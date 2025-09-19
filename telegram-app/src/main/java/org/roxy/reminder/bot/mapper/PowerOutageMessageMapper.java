package org.roxy.reminder.bot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.roxy.reminder.bot.persistence.entity.NotificationEntity;
import org.roxy.reminder.bot.persistence.entity.PowerOutageSourceMessageEntity;
import org.roxy.reminder.common.dto.PowerOutageDto;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PowerOutageMessageMapper {
    PowerOutageSourceMessageEntity mapDtoToEntity(PowerOutageDto source);
    List<PowerOutageSourceMessageEntity> mapDtoToEntity(List<PowerOutageDto> sourceList);
}
