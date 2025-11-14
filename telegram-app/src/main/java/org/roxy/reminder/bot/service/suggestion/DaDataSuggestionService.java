package org.roxy.reminder.bot.service.suggestion;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.mapper.SuggestedAddressMapper;
import org.roxy.reminder.bot.persistence.entity.CityEntity;
import org.roxy.reminder.bot.persistence.entity.LocationEntity;
import org.roxy.reminder.bot.persistence.repository.CityRepository;
import org.roxy.reminder.bot.persistence.repository.LocationRepository;
import org.roxy.reminder.bot.service.suggestion.client.*;
import org.roxy.reminder.bot.service.suggestion.dto.DaDataSuggestionResponseDto;
import org.roxy.reminder.bot.service.suggestion.dto.LocationDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DaDataSuggestionService implements SuggestionService {

    private final LocationRepository locationRepository;
    private final CityRepository cityRepository;
    private final DaDataWebClient daDataWebClient;
    private final SuggestedAddressMapper mapper;
    private final int MAX_SUGGESTIONS_COUNT = 10;

    public DaDataSuggestionService(LocationRepository locationRepository, CityRepository cityRepository,
                                   DaDataWebClient daDataWebClient, SuggestedAddressMapper mapper) {
        this.locationRepository = locationRepository;
        this.cityRepository = cityRepository;
        this.daDataWebClient = daDataWebClient;
        this.mapper = mapper;
    }

    @Override
    public List<LocationDto> getStreetSuggestions(String locationRestrictionFiasId, String text) {
        String cityType = getCityType(locationRestrictionFiasId);
        return getStreets(locationRestrictionFiasId, cityType, text);
    }

    @Override
    public List<LocationDto> getStreetSuggestions(String text) {
        return getStreets(text);
    }

    private List<LocationDto> getStreets(String locationRestrictionFiasId, String type, String text) {
        HashMap<String, String> location = new HashMap<>();
        if (type.equalsIgnoreCase("город")) {
            location.put("city_fias_id", locationRestrictionFiasId);
        } else {
            location.put("settlement_fias_id", locationRestrictionFiasId);
        }
        List<DaDataSuggestionResponseDto> suggestions = daDataWebClient.getSuggestions(
                DaDataSearchRequest.builder()
                        .query(text)
                        .locations(
                                List.of(location)
                        ).count(MAX_SUGGESTIONS_COUNT)
                        //.fromBound(new DaDataSearchRequest.Bound("street"))
                        //.toBound(new DaDataSearchRequest.Bound("street"))
                        .restrictValue(true)
                        .build()
        ).getSuggestions();
        if (suggestions.isEmpty()) {
            return Collections.emptyList();
        }
        saveSuggestedAddresses(suggestions);
        return suggestions.stream()
                .map(DaDataSuggestionResponseDto::getData)
                .map(mapper::mapDaDataResponseItemToLocationDto)
                .collect(Collectors.toList());
    }

    private List<LocationDto> getStreets(String userInput) {
        List<DaDataSuggestionResponseDto> suggestions = daDataWebClient.getSuggestions(
                DaDataSearchRequest.builder()
                        .query(userInput)
                        .count(MAX_SUGGESTIONS_COUNT)
                        // .fromBound(new DaDataSearchRequest.Bound("street"))
                        //.toBound(new DaDataSearchRequest.Bound("street"))
                        .restrictValue(true)
                        .build()
        ).getSuggestions();
        if (suggestions.isEmpty()) {
            log.warn("Could not find suggestions for {}", userInput);
            return Collections.emptyList();
        }
        saveSuggestedAddresses(suggestions);
        return suggestions.stream()
                .map(DaDataSuggestionResponseDto::getData)
                .map(mapper::mapDaDataResponseItemToLocationDto)
                .collect(Collectors.toList());
    }

    private void saveSuggestedAddresses(List<DaDataSuggestionResponseDto> suggestions) {
        for (DaDataSuggestionResponseDto suggestion : suggestions) {
            try {
                LocationEntity locationEntity = mapper.mapDaDataResponseItemToEntity(suggestion.getData());
                locationRepository.findById(locationEntity.getLocationFiasId()).orElseGet(() -> locationRepository.save(locationEntity));
            } catch (Exception e) {
                log.warn("Failed to save street {}. Error : {} ", suggestion.getData(), e.getMessage());
            }
        }
    }

    private String getCityType(String fiasId) {
        return cityRepository.findById(fiasId)
                .map(CityEntity::getType)
                .orElseThrow(() -> new IllegalArgumentException("City not found for ID: " + fiasId));
    }
}