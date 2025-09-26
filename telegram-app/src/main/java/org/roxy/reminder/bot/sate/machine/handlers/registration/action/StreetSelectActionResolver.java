package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.service.UserCartService;
import org.roxy.reminder.bot.service.broker.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.service.webclient.dto.message.request.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StreetSelectActionResolver extends ActionResolver {

    @Autowired
    private UserCartService userCartService;

    @Override
    public Event resolveAction(UpdateDto update) {
        log.info("Handling message = {}", update);
        UserCartEntity userCart = userCartService.getUserCartByChatId(update.getChatId());
        userCartService.addStreet(userCart.getId(),update.getUserResponse());
        super.botClient.sendMessage( MessageDto.builder()
                .chatId(String.valueOf(update.getChatId()))
                .text("Всё получилось. Мы отправим уведомление, если на вашей улице будет запланировано отключение света.")
                .build());
        return Event.REPLY_RECEIVED;
    }
}