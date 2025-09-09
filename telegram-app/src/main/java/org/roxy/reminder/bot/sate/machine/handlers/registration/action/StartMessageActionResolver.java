package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.tgclient.dto.message.request.keyboard.InlineKeyboardDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartMessageActionResolver extends ActionResolver {

    @Override
    public Event resolveAction(UpdateDto update) {
        log.info("Handling message = {}", update);
        //TODO Добавить удаление старой записи из БД
        MessageDto citySelectMessage =
                MessageDto.builder()
                        .chatId(String.valueOf(update.getChatId()))
                        .text("Выберите город")
                        .replyMarkup(
                                new InlineKeyboardDto.KeyboardBuilder()
                                        .addRow()
                                        .addButton("Ростов", "Ростов-на-Дону")
                                        .addButton("Аксай", "Аксай")
                                        .addRow()
                                        .addButton("Новочеркасск", "Новочеркасск")
                                        .build())
                        .build();

        super.botClient.sendMessage(citySelectMessage);
        return Event.REPLY_RECEIVED;
    }
}
