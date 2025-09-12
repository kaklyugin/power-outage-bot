package org.roxy.reminder.bot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.roxy.reminder.bot.persistence.entity.NotificationEntity;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationEntityToTgMessageMapper {

    @Mapping(target = "chatId", source = "source.userCart.chatId")
    @Mapping(target = "text", source = "source.notificationText")
    MessageDto mapNotificationEntityToMessageDto(NotificationEntity source);

    List<MessageDto> mapNotificationEntityToMessageDto(List<NotificationEntity> sourceList);

}
