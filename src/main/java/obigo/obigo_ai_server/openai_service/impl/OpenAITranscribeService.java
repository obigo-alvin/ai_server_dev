package obigo.obigo_ai_server.openai_service.impl;

import obigo.obigo_ai_server.core.AiTranscribePort;
import obigo.obigo_ai_server.openai_service.OpenAIService;
import obigo.obigo_ai_server.prompt.PromptService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAITranscribeService implements AiTranscribePort {

    private final OpenAIService openApi;
    private String apiKey;
    private final PromptService promptService;

//    @jakarta.annotation.PostConstruct
//    void init() {
//        apiKey = System.getenv("OPENAI_API_KEY");
//        if (apiKey == null || apiKey.isBlank()) log.error("OPENAI_API_KEY is not set");
//    }
    private static final String DEFAULT_TRANSCRIBE_PROMPT_PATH = "prompts/transcribe_ko_v1.md";

    @Override
    public JsonNode transcribe(MultipartFile file, String model, String temperature, String prompt) throws IOException {
        if (file==null || file.isEmpty()) throw new IllegalArgumentException("file is required");

        // 1) 기본 프롬프트 로딩 (클라이언트가 prompt를 안 주면 서버 기본값 사용)
        String usedPrompt = (prompt != null && !prompt.isBlank())
                ? prompt
                : promptService.get(DEFAULT_TRANSCRIBE_PROMPT_PATH);

        String usedModel = (model!=null && !model.isBlank()) ? model : "gpt-4o-transcribe";
        String usedTemp  = (temperature!=null && !temperature.isBlank()) ? temperature : "0";

        String filename = (file.getOriginalFilename()!=null) ? file.getOriginalFilename() : "upload.bin";
        String mime = guessMime(filename);

        RequestBody fileBody = RequestBody.create(file.getBytes(), MediaType.parse(mime));
        MultipartBody.Part fpart = MultipartBody.Part.createFormData("file", filename, fileBody);
        RequestBody modelPart = RequestBody.create(usedModel, MediaType.parse("text/plain"));
        RequestBody tempPart  = RequestBody.create(usedTemp,  MediaType.parse("text/plain"));
        RequestBody promptPart= RequestBody.create(usedPrompt,MediaType.parse("text/plain"));

        Response<JsonNode> r = openApi.transcribe(fpart, modelPart, tempPart, promptPart).execute();
        if (!r.isSuccessful() || r.body()==null) {
            throw new IOException("OpenAI transcribe error: " + r.code() + " - " + (r.errorBody()!=null ? r.errorBody().string() : "Unknown"));
        }
        return r.body();
    }

//    private String bearer() { return "Bearer " + apiKey; }
//    private void ensureKey() { if (apiKey == null || apiKey.isBlank()) throw new IllegalStateException("OPENAI_API_KEY is not set"); }

    private String guessMime(String filename) {
        String fn = filename.toLowerCase();
        if (fn.endsWith(".mp3")) return "audio/mpeg";
        if (fn.endsWith(".m4a") || fn.endsWith(".mp4")) return "audio/mp4";
        if (fn.endsWith(".wav")) return "audio/wav";
        return "application/octet-stream";
    }
}
