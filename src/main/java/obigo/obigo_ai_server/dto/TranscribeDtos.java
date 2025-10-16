package obigo.obigo_ai_server.dto;

import lombok.Data;

@Data
public class TranscribeDtos {
    private String model;        // default gpt-4o-transcribe
    private String temperature;  // default "0"
    private String prompt;       // default ""
}
