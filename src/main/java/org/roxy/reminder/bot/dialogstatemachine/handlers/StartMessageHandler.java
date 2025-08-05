package org.roxy.reminder.bot.dialogstatemachine.handlers;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.dialogstatemachine.enums.DialogStatus;
import org.roxy.reminder.bot.dialogstatemachine.storage.ChatContext;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.tgclient.dto.message.request.keyboard.InlineKeyboardDto;
import org.roxy.reminder.bot.tgclient.service.http.HttpBotClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartMessageHandler implements UpdateHandler {

    // @Autowired, чтобы в классе, где создаём List<Handlers> не надо было прокидывать HttpBotClient
    @Autowired
    private HttpBotClient botClient;

    @Override
    public boolean isApplicable(UpdateDto update, ChatContext context) {
        return update.getUserResponse().equals("/start");
    }

    @Override
    public void handleUpdate(UpdateDto update, ChatContext context) {
        log.info("Handling message = {}", update);
        //TODO Добавить удаление старой записи из БД
        MessageDto selectCityMessage = MessageDto.builder()
                .chatId(String.valueOf(update.getChatId()))
                .text("Выберите город")
                .replyMarkup(
                        new InlineKeyboardDto.KeyboardBuilder()
                                .addRow()
                                .addButton("Ростов", "CALLBACK_ROSTOV")
                                .addButton("Аксай", "CALLBACK_AKSAI")
                                .addRow()
                                .addButton("Новочеркасск", "CALLBACK_NOVOCHERKASSK")
                                .build())
                .build();
        Long messageId = botClient.sendMessage(selectCityMessage).getMessageId();
        context.setLastBotMessageId(messageId);
        context.setStatus(DialogStatus.WAITING_FOR_CITY_INPUT);
        log.info("Status = {}", context.getStatus());
    }
}
