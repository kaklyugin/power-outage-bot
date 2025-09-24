package org.roxy.reminder.bot.service.suggestion;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.mapper.SuggestedAddressMapper;
import org.roxy.reminder.bot.persistence.entity.StreetEntity;
import org.roxy.reminder.bot.service.suggestion.client.*;
import org.roxy.reminder.bot.persistence.repository.StreetRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SuggestionService {

    private final StreetRepository streetRepository;
    private final DaDataWebClient daDataWebClient;
    private final SuggestedAddressMapper mapper;
    private final int MAX_SUGGESTIONS_COUNT = 10;

    public SuggestionService(StreetRepository streetRepository,
                             DaDataWebClient daDataWebClient, SuggestedAddressMapper mapper) {
        this.streetRepository = streetRepository;
        this.daDataWebClient = daDataWebClient;
        this.mapper = mapper;
    }

    public List<String> getStreetSuggestions(String fiasId, String cityType, String userInput
    ) {
        return getStreetsFromDaDataService(fiasId, cityType, userInput);
    }

    private List<String> getStreetsFromDaDataService(String locationRestrictionFiasId, String type, String userInput) {
        HashMap<String, String> location = new HashMap<>();
        if (type.equalsIgnoreCase("город")) {
            location.put("city_fias_id", locationRestrictionFiasId);
        } else {
            location.put("settlement_fias_id", locationRestrictionFiasId);
        }
        List<SuggestionDto> suggestions = daDataWebClient.getSuggestions(
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
            return Collections.emptyList();
        }
        // TODO Кэшировать в редисе fiasId
        for (SuggestionDto suggestion : suggestions) {
            try {
                StreetEntity streetEntity = mapper.mapStreetDtoToEntity(suggestion.getData());
                streetRepository.save(streetEntity);
            } catch (Exception e) {
                log.warn("Failed to save street {}. Error : {} ", suggestion.getData(), e.getMessage());
            }
        }

        return suggestions.stream()
                .map(SuggestionDto::getValue)
                .collect(Collectors.toList());
    }
}