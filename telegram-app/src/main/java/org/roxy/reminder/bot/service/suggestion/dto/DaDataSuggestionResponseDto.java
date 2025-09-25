package org.roxy.reminder.bot.service.suggestion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DaDataSuggestionResponseDto {

    @JsonProperty("value")
    private String value;

    @JsonProperty("unrestricted_value")
    private String unrestrictedValue;

    @JsonProperty("data")
    private DaDataAddressResponseDto data;
}