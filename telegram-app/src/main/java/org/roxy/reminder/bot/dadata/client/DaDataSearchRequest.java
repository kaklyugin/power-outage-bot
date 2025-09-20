package org.roxy.reminder.bot.dadata.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DaDataSearchRequest {

    @JsonProperty("query")
    private String query;

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("locations")
    private List<Map<String,String>> locations;

    @JsonProperty("from_bound")
    private Bound fromBound;

    @JsonProperty("to_bound")
    private Bound toBound;

    @JsonProperty("restrict_value")
    private Boolean restrictValue;

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Bound {
        @JsonProperty("value")
        private String value;
    }
}