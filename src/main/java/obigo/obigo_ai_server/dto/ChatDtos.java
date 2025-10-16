package obigo.obigo_ai_server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

public class ChatDtos {
    @Data
    public static class ChatMessage {
        @NotBlank
        public String role;
        @NotBlank
        public String content;
    }

    @Data
    public static class ChatRequest {
        private String message;
        private List<ChatMessage> messages;
        private String model;        // default gpt-4-turbo
        private Double temperature;  // default 0.7
    }
    @Data
    public static class ChatResponse {
        private String model;
        private String content;
        private String finishReason;
        private String out;
    }
}
