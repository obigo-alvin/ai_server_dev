package obigo.obigo_ai_server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "ai")
public class AiProps {
    private OpenAi openai = new OpenAi();
    private Google google = new Google();

    @Data
    public static class OpenAi {
        private String apiKeyEnv = "OPENAI_API_KEY";
        private String baseUrl = "https://api.openai.com/";
        private String defaultChatModel = "gpt-4-turbo";
        private double defaultTemp = 0.7;
    }

    @Data
    public static class Google {
        private String apiKeyEnv = "GOOGLE_VISION_KEY";
        private String baseUrl = "https://vision.googleapis.com/";
        private Cred credentials = new Cred();
        private java.util.List<String> scopes;

        @Data
        public static class Cred {
            private String path;
            private String json;
        }
    }
}
