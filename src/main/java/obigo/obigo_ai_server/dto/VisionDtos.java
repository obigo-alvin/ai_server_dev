package obigo.obigo_ai_server.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

public class VisionDtos {

    @Data
    public static class AnnotateRequest {
        /**
         * Vision Feature 타입:
         * TEXT_DETECTION, DOCUMENT_TEXT_DETECTION, LABEL_DETECTION, FACE_DETECTION,
         * LANDMARK_DETECTION, LOGO_DETECTION, OBJECT_LOCALIZATION, SAFE_SEARCH_DETECTION 등
         */
        @NotEmpty
        private List<String> features;

        // 옵션 (감지 결과 수 제한 등)
        private Integer maxResults;   // 각 feature 공통 maxResults
        // 언어 힌트 등 필요시 확장 가능
    }
}
