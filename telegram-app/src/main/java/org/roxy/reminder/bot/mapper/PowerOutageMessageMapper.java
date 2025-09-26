package org.roxy.reminder.bot.mapper;

import org.mapstruct.Mapper;
import org.roxy.reminder.bot.persistence.entity.PowerOutageSourceMessageEntity;
import org.roxy.reminder.bot.service.broker.dto.PowerOutageDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PowerOutageMessageMapper {
    PowerOutageSourceMessageEntity mapDtoToEntity(PowerOutageDto source);
    List<PowerOutageSourceMessageEntity> mapDtoToEntity(List<PowerOutageDto> sourceList);
}
