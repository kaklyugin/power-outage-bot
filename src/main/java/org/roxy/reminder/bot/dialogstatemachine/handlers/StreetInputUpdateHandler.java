package org.roxy.reminder.bot.dialogstatemachine.handlers;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.dialogstatemachine.enums.DialogStatus;
import org.roxy.reminder.bot.dialogstatemachine.storage.ChatContext;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.service.UserCartService;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.tgclient.dto.updates.UpdateType;
import org.roxy.reminder.bot.tgclient.service.http.HttpBotClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StreetInputUpdateHandler implements UpdateHandler {
    @Autowired
    private HttpBotClient botClient;
    @Autowired
    private UserCartService userCartService;

    @Override
    public boolean isApplicable(UpdateDto update, ChatContext context) {
        return (
                context.getStatus().equals(DialogStatus.WAITING_FOR_STREET_INPUT)
                        && update.getUpdateType().equals(UpdateType.TEXT)
        );
    }

    @Override
    public void handleUpdate(UpdateDto update, ChatContext context) {
        log.info("Handling message = {}", update);

        MessageDto cityInputQuestionMessage = MessageDto.builder()
                .chatId(String.valueOf(update.getChatId()))
                .text("Всё получилось. Мы отправим уведомление, если на вашей улице будет запланировано отключение света.")
                .build();
        botClient.sendMessage(cityInputQuestionMessage);

        userCartService.saveUserCart(update.getChatId(),
                context.getCity(),
                context.getStreet()
        );
        context.setStatus(DialogStatus.DONE);
    }
}