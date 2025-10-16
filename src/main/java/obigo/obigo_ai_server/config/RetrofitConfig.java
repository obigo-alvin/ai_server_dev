package obigo.obigo_ai_server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RetrofitConfig {

    private final AiProps props;

    @Bean
    public OkHttpClient baseClient() {
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .addInterceptor(log)
                .callTimeout(Duration.ofSeconds(90))
                .connectTimeout(Duration.ofSeconds(30))
                .readTimeout(Duration.ofSeconds(90))
                .build();
    }

    // OpenAI
    @Bean
    public Retrofit openAiRetrofit(OkHttpClient baseClient, ObjectMapper om) {
        String apiKey = System.getenv(props.getOpenai().getApiKeyEnv());
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Missing OpenAI API key in env: " + props.getOpenai().getApiKeyEnv());
        }
        OkHttpClient client = baseClient.newBuilder()
                .addInterceptor(chain -> {
                    Request req = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + apiKey)
                            .build();
                    return chain.proceed(req);
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(props.getOpenai().getBaseUrl())
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(om))
                .build();
    }

    // Google Vision
    @Bean
    public OkHttpClient googleVisionClient(OkHttpClient baseClient, ObjectMapper om) {
        String apiKey = System.getenv(props.getGoogle().getApiKeyEnv());

        // 1) API Key 모드: ?key=... 자동 부착
        if (apiKey != null && !apiKey.isBlank()) {
            Interceptor apiKeyInterceptor = chain -> {
                var req = chain.request();
                HttpUrl url = req.url().newBuilder().addQueryParameter("key", apiKey).build();
                Request newReq = req.newBuilder().url(url).build();
                return chain.proceed(newReq);
            };
            return baseClient.newBuilder().addInterceptor(apiKeyInterceptor).build();
        }

        // 2) OAuth2 모드: 서비스계정/ADC로 Bearer 주입
        GoogleCredentials credentials = loadGoogleCredentials(); // ← 지연 로딩
        Interceptor oauthInterceptor = chain -> {
            synchronized (credentials) {
                credentials.refreshIfExpired();
                AccessToken token = credentials.getAccessToken();
                Request req = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token.getTokenValue())
                        .build();
                return chain.proceed(req);
            }
        };
        return baseClient.newBuilder().addInterceptor(oauthInterceptor).build();
    }

    @Bean
    public Retrofit googleVisionRetrofit(OkHttpClient googleVisionClient, ObjectMapper om) {
        return new Retrofit.Builder()
                .baseUrl(props.getGoogle().getBaseUrl())
                .client(googleVisionClient)
                .addConverterFactory(JacksonConverterFactory.create(om))
                .build();
    }

    // helper: 서비스계정/ADC 로딩 (필요할 때만 호출)
    private GoogleCredentials loadGoogleCredentials() {
        AiProps.Google g = props.getGoogle();
        try {
            if (g.getCredentials().getJson() != null && !g.getCredentials().getJson().isBlank()) {
                try (var in = new ByteArrayInputStream(g.getCredentials().getJson().getBytes())) {
                    return GoogleCredentials.fromStream(in).createScoped(scopesOrDefault(g));
                }
            }
            if (g.getCredentials().getPath() != null && !g.getCredentials().getPath().isBlank()) {
                try (var in = new FileInputStream(g.getCredentials().getPath())) {
                    return GoogleCredentials.fromStream(in).createScoped(scopesOrDefault(g));
                }
            }
            // 마지막 수단: ADC
            return GoogleCredentials.getApplicationDefault().createScoped(scopesOrDefault(g));
        } catch (Exception e) {
            throw new IllegalStateException(
                "GoogleCredentials not found. Set ai.google.credentials.path/json or GOOGLE_VISION_KEY (API Key mode). Root cause: " + e.getMessage(), e);
        }
    }

    private List<String> scopesOrDefault(AiProps.Google g) {
        return (g.getScopes() == null || g.getScopes().isEmpty())
                ? List.of("https://www.googleapis.com/auth/cloud-platform")
                : g.getScopes();
    }
}