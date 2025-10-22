package org.roxy.reminder.bot.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.mapper.PowerOutageMessageMapper;
import org.roxy.reminder.bot.persistence.entity.PowerOutageSourceMessageEntity;
import org.roxy.reminder.bot.persistence.repository.PowerOutageSourceMessageRepository;
import org.roxy.reminder.bot.service.formatter.AddressFormatterService;
import org.roxy.reminder.bot.service.broker.dto.PowerOutageDto;
import org.roxy.reminder.bot.service.suggestion.StreetDto;
import org.roxy.reminder.bot.service.suggestion.SuggestionService;
import org.roxy.reminder.bot.util.AddressComponents;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OutageMessageService {
    private final PowerOutageSourceMessageRepository repository;
    private final AddressFormatterService addressFormatterService;
    private final PowerOutageMessageMapper messageMapper;
    private final SuggestionService suggestionService;

    public OutageMessageService(PowerOutageSourceMessageRepository repository, AddressFormatterService addressFormatterService, PowerOutageMessageMapper messageMapper, SuggestionService suggestionService) {
        this.repository = repository;
        this.addressFormatterService = addressFormatterService;
        this.messageMapper = messageMapper;
        this.suggestionService = suggestionService;
    }

    @Transactional
    public PowerOutageSourceMessageEntity saveMessage(PowerOutageDto powerOutageDto) {
        Optional<PowerOutageSourceMessageEntity> entity = repository.findByMessageHashCode(powerOutageDto.getMessageHashCode());
        if (entity.isEmpty()) {
            PowerOutageSourceMessageEntity newEntity = messageMapper.mapDtoToEntity(powerOutageDto);
            AddressComponents addressComponents = addressFormatterService.extractAddressComponents(newEntity.getAddress());
            newEntity.setStreetType(addressComponents.getStreetType());
            newEntity.setStreetName(addressComponents.getStreetName());
            newEntity.setBuildingsNumbers(addressComponents.getBuildingsNumbers());
            String streetFiasId = findStreetFiasId(powerOutageDto.getCity(), addressComponents);
            newEntity.setStreetFiasId(streetFiasId);
            return repository.save(newEntity);
        }
        return entity.get();
    }


    private String findStreetFiasId(String city, AddressComponents addressComponents ) {
        String text  = "Ростовская область, " + city + " " + addressComponents.getStreetType() + " " + addressComponents.getStreetName();
        StreetDto streetDto = suggestionService.getStreetSuggestions(text).getFirst();
        return streetDto != null ? streetDto.getFiasId() : null;
    }
}