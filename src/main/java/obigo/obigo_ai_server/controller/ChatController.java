package obigo.obigo_ai_server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import obigo.obigo_ai_server.core.AiChatPort;
import obigo.obigo_ai_server.dto.ChatDtos.ChatRequest;
import obigo.obigo_ai_server.dto.ChatDtos.ChatResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ChatController {
    private final AiChatPort chatPort;

    @PostMapping(value = "/task-plans")
    public ResponseEntity<JsonNode> chat(@RequestBody @Valid ChatRequest req) throws Exception {
        return ResponseEntity.ok(chatPort.chat(req));
    }

    @GetMapping("/health")
    public Object health() { return java.util.Map.of("ok", true); }
}
