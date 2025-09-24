package org.roxy.reminder.bot.service.suggestion.client;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DaDataSuggestionsResponse {

    @JsonProperty("suggestions")
    private List<SuggestionDto> suggestions;



}