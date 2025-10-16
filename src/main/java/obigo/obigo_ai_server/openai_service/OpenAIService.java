package obigo.obigo_ai_server.openai_service;

import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface OpenAIService {
    @POST("v1/chat/completions")
    Call<JsonNode> chat(@Body Map<String, Object> body);

    @Multipart
    @POST("v1/audio/transcriptions")
    Call<JsonNode> transcribe(@Part MultipartBody.Part file,
                              @Part("model") RequestBody model,
                              @Part("temperature") RequestBody temperature,
                              @Part("prompt") RequestBody prompt);
}
