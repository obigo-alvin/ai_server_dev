package obigo.obigo_ai_server.controller;

import obigo.obigo_ai_server.core.AiTranscribePort;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class TranscribeController {
    private final AiTranscribePort transcribePort;

    @PostMapping(value = "/transcriptions")
    public ResponseEntity<JsonNode> transcribe(
            @RequestPart("file") MultipartFile file,
            @RequestPart(name = "model", required = false) String model,
            @RequestPart(name = "temperature", required = false) String temperature,
            @RequestPart(name = "prompt", required = false) String prompt) throws Exception {
        return ResponseEntity.ok(transcribePort.transcribe(file, model, temperature, prompt));
    }
}
