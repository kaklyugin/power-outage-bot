package org.roxy.reminder.bot.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.mapper.PowerOutageMessageMapper;
import org.roxy.reminder.bot.persistence.entity.PowerOutageSourceMessageEntity;
import org.roxy.reminder.bot.persistence.repository.PowerOutageSourceMessageRepository;
import org.roxy.reminder.bot.service.formatter.AddressFormatter;
import org.roxy.reminder.bot.service.broker.dto.PowerOutageDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class OutageMessageService {
    private final PowerOutageSourceMessageRepository repository;
    private final AddressFormatter addressFormatter;
    private final PowerOutageMessageMapper messageMapper;

    public OutageMessageService(PowerOutageSourceMessageRepository repository, AddressFormatter addressFormatter, PowerOutageMessageMapper messageMapper) {
        this.repository = repository;
        this.addressFormatter = addressFormatter;
        this.messageMapper = messageMapper;
    }
    @Transactional
    public PowerOutageSourceMessageEntity saveMessage(PowerOutageDto powerOutageDto)
    {
        PowerOutageSourceMessageEntity entity = messageMapper.mapDtoToEntity(powerOutageDto);
        entity = normalizeAddress(entity);
        return repository.save(entity);
    }

    private PowerOutageSourceMessageEntity normalizeAddress(PowerOutageSourceMessageEntity entity)
    {
        String address = entity.getAddress();
        List<String> addressTokens = Arrays.stream(address.split(" ")).toList();
        if(addressTokens.size() > 1)
        {
            entity.setNormalizedStreetType(
                    addressFormatter.normalizeStreetType(addressTokens.getFirst()));
        }
        return entity;
    }

}
