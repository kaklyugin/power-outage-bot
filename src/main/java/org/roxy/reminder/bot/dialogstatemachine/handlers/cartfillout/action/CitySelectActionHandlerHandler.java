package org.roxy.reminder.bot.dialogstatemachine.handlers.cartfillout.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.dialogstatemachine.enums.Event;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CitySelectActionHandlerHandler implements ActionHandler {

    @Autowired
    private UserCartRepository userCartRepository;

    @Override
    public ActionResponseDto handleAction(UpdateDto update, UserCartEntity userCart) {
        log.info("Handling update = {}", update);
        userCart.setCity(update.getUserResponse());
        userCartRepository.save(userCart);

        MessageDto cityInputMessage =  MessageDto.builder()
                .chatId(String.valueOf(update.getChatId()))
                .text("Введите улицу")
                .build();

        return ActionResponseDto.builder()
                .message(cityInputMessage)
                .event(Event.REPLY_RECEIVED)
                .build();
    }
}