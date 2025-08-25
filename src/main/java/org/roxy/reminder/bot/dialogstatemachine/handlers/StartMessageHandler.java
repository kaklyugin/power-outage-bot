package org.roxy.reminder.bot.dialogstatemachine.handlers;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.dialogstatemachine.enums.Event;
import org.roxy.reminder.bot.dialogstatemachine.handlers.dto.HandlerResponse;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.DialogContextEntity;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.tgclient.dto.message.request.keyboard.InlineKeyboardDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartMessageHandler implements UpdateHandler {

    @Override
    public HandlerResponse handleUpdate(UpdateDto update, DialogContextEntity context) {
        log.info("Handling message = {}", update);
        //TODO Добавить удаление старой записи из БД
        MessageDto citySelectMessage =
                MessageDto.builder()
                        .chatId(String.valueOf(update.getChatId()))
                        .text("Выберите город")
                        .replyMarkup(
                                new InlineKeyboardDto.KeyboardBuilder()
                                        .addRow()
                                        .addButton("Ростов", "г.Ростов-на-Дону")
                                        .addButton("Аксай", "г.Аксай")
                                        .addRow()
                                        .addButton("Новочеркасск", "г.Новочеркасск")
                                        .build())
                        .build();

        return
                HandlerResponse.builder()
                        .message(citySelectMessage)
                        .event(Event.REPLY_RECEIVED)
                        .build();
    }
}
