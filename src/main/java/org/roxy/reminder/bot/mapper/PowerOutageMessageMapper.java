package org.roxy.reminder.bot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.roxy.reminder.bot.persistence.entity.PowerOutageNotificationEntity;
import org.roxy.reminder.common.dto.PowerOutageDto;

@Mapper(componentModel = "spring")
public interface PowerOutageMessageMapper {
    @Mapping(target = "reasonMessageHashCode", source = "source.hashCode")
    @Mapping(target = "notificationText", source = "source", qualifiedByName = "createNotificationText")
    PowerOutageNotificationEntity mapDtoToEntity(PowerOutageDto source);

    @Named("createNotificationText")
    default String createNotificationText(PowerOutageDto source) {
        return "Отключение света по адресу " +
                source.getLocation() + " "+
                source.getAddress() + " " +
                " c " + source.getDateTimeOff() +
                " по " + source.getDateTimeOn() +
                ". " +
                "Причина : " + source.getPowerOutageReason();
    }
}
