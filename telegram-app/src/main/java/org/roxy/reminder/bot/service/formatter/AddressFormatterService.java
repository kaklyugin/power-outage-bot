package org.roxy.reminder.bot.service.formatter;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.persistence.dto.LocationTypeDto;
import org.roxy.reminder.bot.persistence.repository.LocationTypeRepository;
import org.roxy.reminder.bot.util.AddressComponents;
import org.roxy.reminder.bot.util.AddressUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AddressFormatterService {
    private final LocationTypeRepository locationTypeRepository;

    public AddressFormatterService(LocationTypeRepository locationTypeRepository) {
        this.locationTypeRepository = locationTypeRepository;
    }

    public AddressComponents extractAddressComponents(String address) {
        AddressComponents addressComponents = AddressUtils.extractAddressComponents(address);
        String normalizedStreetType = normalizeStreetType(addressComponents.getStreetType());
        return new AddressComponents(normalizedStreetType, addressComponents.getStreetName(), addressComponents.getBuildingsNumbers());
    }

    private String normalizeStreetType(String rawStreetType) {
        List<LocationTypeDto> locationTypes = getLocationTypes(LocationCategories.STREET);
        Optional<LocationTypeDto> normalizedType = locationTypes.stream()
                .filter(x -> x.getAlias().equalsIgnoreCase(rawStreetType))
                .findFirst();
        if (normalizedType.isPresent()) {
            return normalizedType.get().getType();
        } else {
            log.warn("Failed to normalize location for {}  Not matching aliases found.", rawStreetType);
        }
        return rawStreetType;
    }

    private List<LocationTypeDto> getLocationTypes(LocationCategories locationCategory) {
        //TODO CACHE
        return locationTypeRepository.getAllByCategory(locationCategory.getValue());
    }
}