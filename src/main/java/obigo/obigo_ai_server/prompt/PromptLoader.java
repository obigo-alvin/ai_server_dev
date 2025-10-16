package obigo.obigo_ai_server.prompt;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class PromptLoader {

    public String load(String classpathLocation) {
        try {
            var res = new ClassPathResource(classpathLocation);
            try (var in = res.getInputStream()) {
                return new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load prompt from: " + classpathLocation, e);
        }
    }
}
