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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
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
            return repository.save(newEntity);
        }
        return entity.get();
    }

    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void enrichWithFiasId()
    {
        List<PowerOutageSourceMessageEntity> recordsForEnriching = repository.findByIsStreetFiasRequestedFalse();
        for (PowerOutageSourceMessageEntity record : recordsForEnriching) {
            String streetFiasId = findStreetFiasId(record.getCity(), record.getStreetType(), record.getStreetName());
            record.setStreetFiasId(streetFiasId);
            record.setStreetFiasRequested(true);
        }
        repository.saveAll(recordsForEnriching);
    }

    private String findStreetFiasId(String city, String streetType, String streetName) {
        String text  = "Ростовская область, " + city + " " + streetType + " " + streetName;

        List<StreetDto> suggestions = suggestionService.getStreetSuggestions(text);

        if( suggestions.isEmpty())
         {
             /*Пробуем без типа улицы потому что Донэнерго иногда переулки называет улицами*/
             text  = "Ростовская область, " + city + " " + streetName;
             suggestions = suggestionService.getStreetSuggestions(text);
         }

        return !suggestions.isEmpty()  ? suggestions.getFirst().getStreetFiasId() : null;
    }
}