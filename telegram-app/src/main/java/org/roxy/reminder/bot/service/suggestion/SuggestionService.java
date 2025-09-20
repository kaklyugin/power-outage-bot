package org.roxy.reminder.bot.service.suggestion;

import org.roxy.reminder.bot.dadata.client.AddressSuggestionsResponse;
import org.roxy.reminder.bot.dadata.client.DaDataSearchRequest;
import org.roxy.reminder.bot.dadata.client.DaDataWebClient;
import org.roxy.reminder.bot.persistence.repository.StreetRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuggestionService {

    private final StreetRepository streetRepository;
    private final DaDataWebClient daDataWebClient;
    private final int MAX_SUGGESTIONS_COUNT = 10;

    public SuggestionService(StreetRepository streetRepository,
                             DaDataWebClient daDataWebClient) {
        this.streetRepository = streetRepository;
        this.daDataWebClient = daDataWebClient;
    }

    public List<String> getStreetSuggestions(String fiasId, String cityType, String userInput
    ) {
        List<String> streets = getStreetsFromInternalStore(fiasId, userInput);
        return !streets.isEmpty() ? streets : getStreetsFromExternalSuggestionService(fiasId, cityType, userInput);
    }

    private List<String> getStreetsFromInternalStore(String cityFiasId, String userInput) {
        return streetRepository.findWithFuzzySearchByCityIdAndName(
                cityFiasId, userInput, MAX_SUGGESTIONS_COUNT);
    }

    private List<String> getStreetsFromExternalSuggestionService(String fiasId, String type, String userInput) {
        HashMap<String, String> location = new HashMap<>();
        if (type.equalsIgnoreCase("город")) {
            location.put("city_fias_id", fiasId);
        } else {
            location.put("settlement_fias_id", fiasId);
        }
        List<AddressSuggestionsResponse.Suggestion> suggestions = daDataWebClient.getSuggestions(
                DaDataSearchRequest.builder()
                        .query(userInput)
                        .locations(
                                List.of(location)
                        ).count(MAX_SUGGESTIONS_COUNT)
                        .fromBound(new DaDataSearchRequest.Bound("street"))
                        .toBound(new DaDataSearchRequest.Bound("street"))
                        .restrictValue(true)
                        .build()
        ).getSuggestions();
        if (suggestions.isEmpty()) {
            return  Collections.emptyList();
        }
        return suggestions.stream()
                .map(AddressSuggestionsResponse.Suggestion::getValue)
                .collect(Collectors.toList());
    }
}