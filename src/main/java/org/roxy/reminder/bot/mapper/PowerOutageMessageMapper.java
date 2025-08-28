package org.roxy.reminder.bot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.roxy.reminder.bot.persistence.entity.NotificationEntity;
import org.roxy.reminder.common.dto.PowerOutageDto;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface PowerOutageMessageMapper {

    @Mapping(target = "powerOutageHash", source = "source.hashCode")
    @Mapping(target = "notificationText", source = "source", qualifiedByName = "createNotificationText")
    NotificationEntity mapDtoToEntity(PowerOutageDto source);

    @Named("createNotificationText")
    default String createNotificationText(PowerOutageDto source) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM (EEEE) HH:mm:ss");
        return "Отключение света по адресу " +
                source.getCity() + " "+
                source.getAddress() + " " +
                " c " + source.getDateTimeOff().format(formatter) +
                " по " + source.getDateTimeOn().format(formatter) +
                ". " +
                "Причина : " + source.getPowerOutageReason();
    }
}
