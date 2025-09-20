package org.roxy.reminder.bot.dadata.client;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressSuggestionsResponse {

    @JsonProperty("suggestions")
    private List<Suggestion> suggestions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Suggestion {

        @JsonProperty("value")
        private String value;

        @JsonProperty("unrestricted_value")
        private String unrestrictedValue;

        @JsonProperty("data")
        private AddressData data;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressData {

        @JsonProperty("postal_code")
        private String postalCode;

        @JsonProperty("city_fias_id")
        private String cityFiasId;

        @JsonProperty("city_with_type")
        private String cityWithType;

        @JsonProperty("city_type")
        private String cityType;

        @JsonProperty("city_type_full")
        private String cityTypeFull;

        @JsonProperty("city")
        private String city;

        @JsonProperty("settlement_fias_id")
        private String settlementFiasId;

        @JsonProperty("settlement_kladr_id")
        private String settlementKladrId;

        @JsonProperty("settlement_with_type")
        private String settlementWithType;

        @JsonProperty("settlement_type")
        private String settlementType;

        @JsonProperty("settlement_type_full")
        private String settlementTypeFull;

        @JsonProperty("settlement")
        private String settlement;

        @JsonProperty("street_fias_id")
        private String streetFiasId;

        @JsonProperty("street_kladr_id")
        private String streetKladrId;

        @JsonProperty("street_with_type")
        private String streetWithType;

        @JsonProperty("street_type")
        private String streetType;

        @JsonProperty("street_type_full")
        private String streetTypeFull;

        @JsonProperty("street")
        private String street;
    }
}