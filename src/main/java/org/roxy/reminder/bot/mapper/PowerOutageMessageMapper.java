package org.roxy.reminder.bot.mapper;

import jakarta.persistence.Column;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.PowerOutageNotificationEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.tgclient.dto.updates.UpdateResponseDto;
import org.roxy.reminder.common.dto.PowerOutageDto;

import java.time.ZonedDateTime;

@Mapper(componentModel = "spring")
public interface PowerOutageMessageMapper {
    @Mapping(target = "reasonMessageHashCode", source = "powerOutageDtoSource.hashCode")
    @Mapping(target = "notification_text", qualifiedByName = "powerOutageDtoSource.creteNotificationText")
    @Mapping(target = "userCart", source = "userCartSource")
    PowerOutageNotificationEntity mapDtoToEntity(PowerOutageDto powerOutageDtoSource, UserCartEntity userCartSource);

    @Named("creteNotificationText")
    default String creteNotificationText(PowerOutageDto source) {
        return "Отключение света по адресу " +
                source.getLocation() + " "+
                source.getAddress() + " " +
                " c " + source.getDateTimeOff() +
                " по " + source.getDateTimeOn() +
                ". " +
                "Причина : " + source.getPowerOutageReason();
    }
}
