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


    @Mapping(target = "notificationText", source = "source", qualifiedByName = "createNotificationText")
    NotificationEntity mapEntityToNotification(PowerOutageSourceMessageEntity source);

    List<PowerOutageSourceMessageEntity> mapDtoToEntity(List<PowerOutageDto> sourceList);

    @Named("createNotificationText")
    default String createNotificationText(PowerOutageSourceMessageEntity source) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM (EEEE) HH:mm:ss");
        return "Отключение света по адресу " +
                source.getCity() + " "+
                source.getAddress() + " " +
                " c " + source.getDateTimeOff().format(formatter) +
                " по " + source.getDateTimeOn().format(formatter) +
                ". " +
                "Причина : " + source.getPowerOutageReason() +
                "Источник : " + source.getUrl();
    }
}
