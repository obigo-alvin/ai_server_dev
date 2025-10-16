package obigo.obigo_ai_server.openai_service.impl;

import obigo.obigo_ai_server.config.AiProps;
import obigo.obigo_ai_server.core.AiChatPort;
import obigo.obigo_ai_server.dto.ChatDtos.ChatRequest;
import obigo.obigo_ai_server.dto.ChatDtos.ChatMessage;
import obigo.obigo_ai_server.openai_service.OpenAIService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import obigo.obigo_ai_server.prompt.PromptService;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIChatService implements AiChatPort {

    private final OpenAIService openApi;
    private String apiKey;
    private final AiProps props;
    private final PromptService promptService;

    private static final String DEFAULT_CHAT_SYSTEM_PROMPT_PATH = "prompts/chat_system_ko_v1.md";
    private static final String DEFAULT_CHAT_USER_PROMPT_PATH = "prompts/chat_user_ko_v1.md";

    @Override
    public JsonNode chat(ChatRequest req) throws IOException {
        // 1) 기본 프롬프트 로딩 (클라이언트가 prompt를 안 주면 서버 기본값 사용)
        String userPrompt = promptService.get(DEFAULT_CHAT_USER_PROMPT_PATH);
        String systemPrompt = promptService.get(DEFAULT_CHAT_SYSTEM_PROMPT_PATH);

        String model = (req.getModel()!=null && !req.getModel().isBlank())
                ? req.getModel() : props.getOpenai().getDefaultChatModel();
        double temperature = (req.getTemperature()!=null) ? req.getTemperature() : props.getOpenai().getDefaultTemp();

        // messages 배열이 오면 그대로 사용
        List<Map<String, Object>> messagesPayload = new ArrayList<>();
        if (req.getMessages()!=null && !req.getMessages().isEmpty()) {
            for (ChatMessage m : req.getMessages()) {
                // null/blank 방어 (컨트롤러 @Valid로 대부분 걸러지지만 한 번 더)
                if (m == null) continue;
                String role = (m.getRole()==null) ? "" : m.getRole().trim();
                String content = (m.getContent()==null) ? "" : m.getContent();
                if (!role.isEmpty() && !content.isEmpty()) {
                    messagesPayload.add(Map.of(
                            "role", role,
                            "content", content
                    ));
                }
            }
        }

        // 없으면 기존 단일 message 필드로 구성 (하위 호환)
        if (messagesPayload.isEmpty()) {
            String msg = (req.getMessage()==null) ? "" : req.getMessage().trim();
            if (msg.isEmpty()) {
                throw new IllegalArgumentException("Either 'messages' (array) or 'message' (string) must be provided.");
            }
            messagesPayload.add(Map.of("role","user","content", msg));
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", model);
        payload.put("temperature", temperature);
        payload.put("messages", messagesPayload);

        Response<JsonNode> r = openApi.chat(payload).execute();
        if (!r.isSuccessful() || r.body() == null) {
            String msg = (r.errorBody() != null) ? r.errorBody().string() : "Unknown";
            throw new IOException("OpenAI chat error: " + r.code() + " - " + msg);
        }

        log.info("OpenAI chat response: {}", r.body());
        JsonNode root = r.body();
        return r.body();
//        ChatResponse out = new ChatResponse();
//        out.setModel(root.path("model").asText(model));
//        out.setContent(root.path("choices").path(0).path("message").path("content").asText(""));
//        out.setFinishReason(root.path("choices").path(0).path("finish_reason").asText(""));
//        return out;
    }
}
