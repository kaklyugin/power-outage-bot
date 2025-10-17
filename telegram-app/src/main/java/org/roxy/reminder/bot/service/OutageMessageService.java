package org.roxy.reminder.bot.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.events.PowerOutageMessageEvent;
import org.roxy.reminder.bot.mapper.PowerOutageMessageMapper;
import org.roxy.reminder.bot.persistence.entity.PowerOutageSourceMessageEntity;
import org.roxy.reminder.bot.persistence.repository.PowerOutageSourceMessageRepository;
import org.roxy.reminder.bot.service.formatter.AddressFormatterService;
import org.roxy.reminder.bot.service.broker.dto.PowerOutageDto;
import org.roxy.reminder.bot.service.suggestion.StreetDto;
import org.roxy.reminder.bot.service.suggestion.SuggestionService;
import org.roxy.reminder.bot.util.AddressComponents;
import org.springframework.context.event.EventListener;
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
            return repository.save(newEntity);
        }
        return entity.get();
    }

    @EventListener
    public void eventHandler(PowerOutageMessageEvent event) {
        log.info("Received PowerOutageMessageEvent. Address =  {}", event.getContent().getAddress());

        String text = "Ростовская область, "
                +  event.getContent().getCity()
                + " ";
        text += !event.getContent().getStreetName().isEmpty() ?
                    event.getContent().getStreetType() + " " + event.getContent().getStreetName()
                    :event.getContent().getAddress();

        StreetDto streetDto = suggestionService.getStreetSuggestions(text).getFirst();
        if (streetDto == null) {
            log.error("No suggestion found for inbound message with address = {}", text);
            return;
        }

        PowerOutageSourceMessageEntity entity = repository.findById(event.getContent().getId()).orElseThrow();
        entity.setStreetFiasId(streetDto.getFiasId());
        repository.save(entity);
    }
}
