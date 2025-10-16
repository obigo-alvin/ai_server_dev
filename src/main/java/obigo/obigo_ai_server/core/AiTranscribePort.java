package obigo.obigo_ai_server.core;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface AiTranscribePort {
    JsonNode transcribe(MultipartFile file, String model, String temperature, String prompt) throws IOException;
}