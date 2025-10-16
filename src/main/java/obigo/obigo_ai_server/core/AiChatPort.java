package obigo.obigo_ai_server.core;

import com.fasterxml.jackson.databind.JsonNode;
import obigo.obigo_ai_server.dto.ChatDtos.ChatRequest;

import java.io.IOException;

public interface AiChatPort {
    JsonNode chat(ChatRequest req) throws IOException;
}
