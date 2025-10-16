package obigo.obigo_ai_server.core;

import obigo.obigo_ai_server.dto.VisionDtos.AnnotateRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface VisionAnnotatePort {
    JsonNode annotate(JsonNode Body, String apiKey) throws IOException;
}