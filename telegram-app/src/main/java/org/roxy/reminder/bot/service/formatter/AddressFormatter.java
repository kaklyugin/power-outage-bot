package org.roxy.reminder.bot.service.formatter;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.persistence.dto.LocationTypeDto;
import org.roxy.reminder.bot.persistence.repository.LocationTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AddressFormatter {
    private final LocationTypeRepository locationTypeRepository;

    public AddressFormatter(LocationTypeRepository locationTypeRepository) {
        this.locationTypeRepository = locationTypeRepository;
    }

    public String normalizeStreetName(String streetName) {
        return streetName.toLowerCase().replaceAll("ё", "е");
    }

    public String normalizeStreetType(String rawLocationType)
    {
        List<LocationTypeDto> locationTypes = getLocationTypes(LocationCategories.STREET);
        String formattedLocation = rawLocationType.replaceAll(".", "").toLowerCase();
        Optional<LocationTypeDto> normalizedType = locationTypes.stream()
                .filter(x -> x.getAlias().equalsIgnoreCase(formattedLocation))
                .findFirst();
        if (normalizedType.isPresent()) {
            return normalizedType.get().getType();
        } else {
            log.warn("Failed to normalize location for {}. Not matching aliases found.", rawLocationType);
        }
        return rawLocationType;
    }

    private List<LocationTypeDto> getLocationTypes(LocationCategories locationCategory) {
    //TODO add cache
        return locationTypeRepository.getTypesByCategory(locationCategory.name());
    }
}
