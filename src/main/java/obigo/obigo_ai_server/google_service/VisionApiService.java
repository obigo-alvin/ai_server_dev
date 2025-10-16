package obigo.obigo_ai_server.google_service;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VisionApiService {
    // POST https://vision.googleapis.com/v1/images:annotate
    @POST("v1/images:annotate")
    Call<JsonNode> annotate(@Body JsonNode request, @Query("key") String apiKey);
}
