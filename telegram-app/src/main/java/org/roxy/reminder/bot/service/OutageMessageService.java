package org.roxy.reminder.bot.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.mapper.PowerOutageMessageMapper;
import org.roxy.reminder.bot.persistence.entity.PowerOutageSourceMessageEntity;
import org.roxy.reminder.bot.persistence.repository.PowerOutageSourceMessageRepository;
import org.roxy.reminder.bot.service.formatter.AddressFormatterService;
import org.roxy.reminder.bot.service.broker.dto.PowerOutageDto;
import org.roxy.reminder.bot.util.AddressComponents;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class OutageMessageService {
    private final PowerOutageSourceMessageRepository repository;
    private final AddressFormatterService addressFormatterService;
    private final PowerOutageMessageMapper messageMapper;

    public OutageMessageService(PowerOutageSourceMessageRepository repository,
                                AddressFormatterService addressFormatterService,
                                PowerOutageMessageMapper messageMapper
    ) {
        this.repository = repository;
        this.addressFormatterService = addressFormatterService;
        this.messageMapper = messageMapper;
    }

    @Transactional
    public void saveMessage(PowerOutageDto powerOutageDto) {
        Optional<PowerOutageSourceMessageEntity> existingPowerMessage = repository.findByMessageHashCode(powerOutageDto.getMessageHashCode());
        try {
            if (existingPowerMessage.isEmpty()) {
                Objects.requireNonNull(powerOutageDto.getDateTimeOff());
                Objects.requireNonNull(powerOutageDto.getDateTimeOn());

                PowerOutageSourceMessageEntity newEntity = messageMapper.mapDtoToEntity(powerOutageDto);
                AddressComponents addressComponents = addressFormatterService.extractAddressComponents(newEntity.getAddress());
                newEntity.setStreetType(addressComponents.getStreetType());
                newEntity.setStreetName(addressComponents.getStreetName());
                newEntity.setBuildingsNumbers(addressComponents.getBuildingsNumbers());
                boolean isMessageOutOfDate = powerOutageDto.getDateTimeOff().isBefore(ZonedDateTime.now(ZoneId.of("Europe/Moscow")).with(LocalTime.MIDNIGHT));
                newEntity.setArchived(isMessageOutOfDate);

                repository.save(newEntity);
            }
        } catch (Exception e) {
            log.error("Failed to save message = {}. Reason = {}", powerOutageDto, e.getMessage());
        }
    }
}