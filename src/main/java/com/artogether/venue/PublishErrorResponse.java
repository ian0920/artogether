package com.artogether.venue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishErrorResponse {
    private String message;
    private List<MissingRequirement> missingRequirements;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MissingRequirement {
        private String condition;
        private String suggestion;
    }
}
