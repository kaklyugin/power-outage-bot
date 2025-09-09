package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CitySelectActionResolver extends ActionResolver {

    @Autowired
    private UserCartRepository userCartRepository;

    @Override
    public Event resolveAction(UpdateDto update) {
        log.info("Handling update = {}", update);
        UserCartEntity userCart = userCartRepository.findByChatId(update.getChatId())
                .orElseThrow(() -> new RuntimeException("User cart not found"));
        userCart.setCity(update.getUserResponse());
        userCartRepository.save(userCart);
        super.botClient.sendMessage(MessageDto.builder()
                .chatId(String.valueOf(update.getChatId()))
                .text("Введите улицу")
                .build());
        return Event.REPLY_RECEIVED;
    }
}