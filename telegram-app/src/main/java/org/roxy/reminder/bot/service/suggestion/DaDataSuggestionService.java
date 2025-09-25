package org.roxy.reminder.bot.service.suggestion;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.mapper.SuggestedAddressMapper;
import org.roxy.reminder.bot.persistence.entity.CityEntity;
import org.roxy.reminder.bot.persistence.entity.StreetEntity;
import org.roxy.reminder.bot.persistence.repository.CityRepository;
import org.roxy.reminder.bot.service.suggestion.client.*;
import org.roxy.reminder.bot.persistence.repository.StreetRepository;
import org.roxy.reminder.bot.service.suggestion.dto.DaDataSuggestionResponseDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DaDataSuggestionService implements SuggestionService {

    private final StreetRepository streetRepository;
    private final CityRepository cityRepository;
    private final DaDataWebClient daDataWebClient;
    private final SuggestedAddressMapper mapper;
    private final int MAX_SUGGESTIONS_COUNT = 10;

    public DaDataSuggestionService(StreetRepository streetRepository, CityRepository cityRepository,
                                   DaDataWebClient daDataWebClient, SuggestedAddressMapper mapper) {
        this.streetRepository = streetRepository;
        this.cityRepository = cityRepository;
        this.daDataWebClient = daDataWebClient;
        this.mapper = mapper;
    }

    @Override
    public List<StreetDto> getStreetSuggestions(String fiasId, String userInput) {
        String cityType = getCityType(fiasId);
        return getStreets(fiasId, cityType, userInput);
    }

    private List<StreetDto> getStreets(String locationRestrictionFiasId, String type, String userInput) {
        HashMap<String, String> location = new HashMap<>();
        if (type.equalsIgnoreCase("город")) {
            location.put("city_fias_id", locationRestrictionFiasId);
        } else {
            location.put("settlement_fias_id", locationRestrictionFiasId);
        }
        List<DaDataSuggestionResponseDto> suggestions = daDataWebClient.getSuggestions(
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
        for (DaDataSuggestionResponseDto suggestion : suggestions) {
            try {
                StreetEntity streetEntity = mapper.mapStreetDtoToEntity(suggestion.getData());
                streetRepository.findById(streetEntity.getFiasId()).orElseGet(() -> streetRepository.save(streetEntity));
            } catch (Exception e) {
                log.warn("Failed to save street {}. Error : {} ", suggestion.getData(), e.getMessage());
            }
        }
        return suggestions.stream()
                .map(r-> new StreetDto(r.getValue(),r.getData().getFiasId()))
                .collect(Collectors.toList());
    }

    private String getCityType(String fiasId) {
        return cityRepository.findById(fiasId)
                .map(CityEntity::getType)
                .orElseThrow(() -> new IllegalArgumentException("City not found for ID: " + fiasId));
    }
}