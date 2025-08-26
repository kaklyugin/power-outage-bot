package org.roxy.reminder.bot.dialogstatemachine.handlers.filloutcard.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.DialogContextEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StreetSelectActionHandlerHandler implements ActionHandler {

    @Autowired
    private UserCartRepository userCartRepository;

    @Override
    public ActionResponseDto handleAction(UpdateDto update, UserCartEntity userCart) {
        log.info("Handling message = {}", update);
        userCart.setStreet(update.getUserResponse());
        userCartRepository.save(userCart);

        MessageDto registrationCompleted = MessageDto.builder()
                .chatId(String.valueOf(update.getChatId()))
                .text("Всё получилось. Мы отправим уведомление, если на вашей улице будет запланировано отключение света.")
                .build();

        return ActionResponseDto.builder()
                .message(registrationCompleted)
                .build();
    }
}