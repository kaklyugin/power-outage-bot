package org.roxy.reminder.bot.dialogstatemachine.handlers;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.dialogstatemachine.enums.Event;
import org.roxy.reminder.bot.dialogstatemachine.handlers.dto.HandlerResponse;
import org.roxy.reminder.bot.persistence.entity.DialogContextEntity;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StreetInputUpdateHandler implements UpdateHandler {

    @Autowired
    private UserCartRepository userCartRepository;

    @Override
    public HandlerResponse handleUpdate(UpdateDto update, DialogContextEntity context) {
        log.info("Handling message = {}", update);
        context.setStreet(update.getUserResponse());

        UserCartEntity userCart = new UserCartEntity();
        userCart.setChatId(context.getChatId());
        userCart.setCity(context.getCity());
        userCart.setStreet(context.getStreet());
        userCartRepository.save(userCart);

        MessageDto registrationCompleted = MessageDto.builder()
                .chatId(String.valueOf(update.getChatId()))
                .text("Всё получилось. Мы отправим уведомление, если на вашей улице будет запланировано отключение света.")
                .build();

        return HandlerResponse.builder()
                .event(Event.REPLY_RECEIVED)
                .message(registrationCompleted)
                .build();

    }
}