package obigo.obigo_ai_server.controller;

import obigo.obigo_ai_server.core.VisionAnnotatePort;
import obigo.obigo_ai_server.dto.VisionDtos.AnnotateRequest;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class VisionController {
    private final VisionAnnotatePort visionPort;

    @PostMapping(value = "/text-detections", consumes = "application/json", produces = "application/json")
    public ResponseEntity<JsonNode> annotate(
            @RequestParam("key") String apiKey,
            @RequestBody JsonNode body) throws Exception {
        return ResponseEntity.ok(visionPort.annotate(body, apiKey));
    }
}
