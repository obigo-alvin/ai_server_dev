package obigo.obigo_ai_server.config;

import obigo.obigo_ai_server.openai_service.OpenAIService;
import obigo.obigo_ai_server.google_service.VisionApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

@Configuration
@RequiredArgsConstructor
public class ClientBeans {

    private final Retrofit openAiRetrofit;
    private final Retrofit googleVisionRetrofit;

    @Bean
    public OpenAIService openAIService() {
        return openAiRetrofit.create(OpenAIService.class);
    }

    @Bean
    public VisionApiService visionApi() {
        return googleVisionRetrofit.create(VisionApiService.class);
    }
}
