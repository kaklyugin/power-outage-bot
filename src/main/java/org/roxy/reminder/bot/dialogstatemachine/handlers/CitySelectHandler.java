package org.roxy.reminder.bot.dialogstatemachine.handlers;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.dialogstatemachine.enums.Event;
import org.roxy.reminder.bot.dialogstatemachine.handlers.dto.HandlerResponse;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.DialogContextEntity;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CitySelectHandler implements UpdateHandler {

    @Override
    public HandlerResponse handleUpdate(UpdateDto update, DialogContextEntity context) {
        log.info("Handling update = {}", update);
        context.clear();
        context.setCity(update.getUserResponse());

        MessageDto cityInputMessage =  MessageDto.builder()
                .chatId(String.valueOf(update.getChatId()))
                .text("Введите улицу")
                .build();

        return HandlerResponse.builder()
                .message(cityInputMessage)
                .event(Event.REPLY_RECEIVED)
                .build();
    }
}