package org.roxy.reminder.bot.service.suggestion.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDataDto {

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

    @JsonProperty("street_with_type")
    private String streetWithType;

    @JsonProperty("street_type")
    private String streetType;

    @JsonProperty("street_type_full")
    private String streetTypeFull;

    @JsonProperty("street")
    private String street;

    @JsonProperty("fias_id")
    private String fiasId;
}