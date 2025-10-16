package obigo.obigo_ai_server.google_service.impl;

import obigo.obigo_ai_server.core.VisionAnnotatePort;
import obigo.obigo_ai_server.dto.VisionDtos.AnnotateRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import obigo.obigo_ai_server.google_service.VisionApiService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Response;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleVisionService implements VisionAnnotatePort {

    private final VisionApiService api;

    @Override
    public JsonNode annotate(JsonNode body, String apiKey) throws IOException {
        log.debug("annotate apiKey : " + apiKey);
//        if (apiKey==null || apiKey.isEmpty()) throw new IllegalArgumentException("image file is required");
//        if (options==null || options.getFeatures()==null || options.getFeatures().isEmpty())
//            throw new IllegalArgumentException("features is required");
//
//        String base64 = Base64.getEncoder().encodeToString(image.getBytes());
//
//        ArrayNode features = JsonNodeFactory.instance.arrayNode();
//        for (String f : options.getFeatures()) {
//            ObjectNode feature = JsonNodeFactory.instance.objectNode();
//            feature.put("type", f);
//            if (options.getMaxResults()!=null) feature.put("maxResults", options.getMaxResults());
//            features.add(feature);
//        }
//
//        ObjectNode imageNode = JsonNodeFactory.instance.objectNode();
//        imageNode.put("content", base64);
//
//        ObjectNode request = JsonNodeFactory.instance.objectNode();
//        request.set("image", imageNode);
//        request.set("features", features);
//
//        ArrayNode requests = JsonNodeFactory.instance.arrayNode().add(request);
//        ObjectNode root = JsonNodeFactory.instance.objectNode().set("requests", requests);

        Response<JsonNode> r = api.annotate(body, apiKey).execute();
        if (!r.isSuccessful() || r.body()==null) {
            throw new IOException("Vision annotate error: " + r.code() + " - " + (r.errorBody()!=null? r.errorBody().string() : "Unknown"));
        }
        return r.body();
    }
}
