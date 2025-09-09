package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.roxy.reminder.common.util.AddressFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.roxy.reminder.bot.sate.machine.enums.Event.REPLY_RECEIVED;

@Slf4j
@Component
public class StreetSelectActionResolver extends ActionResolver {

    @Autowired
    private UserCartRepository userCartRepository;

    @Override
    public Event resolveAction(UpdateDto update) {
        log.info("Handling message = {}", update);
        UserCartEntity userCart = userCartRepository.findByChatId(update.getChatId())
                .orElseThrow(() -> new RuntimeException("User cart not found"));
        userCart.setStreet(update.getUserResponse());
        userCart.setNormalizedStreet(AddressFormatter.normalizeStreetName(update.getUserResponse()));
        userCartRepository.save(userCart);
        super.botClient.sendMessage( MessageDto.builder()
                .chatId(String.valueOf(update.getChatId()))
                .text("Всё получилось. Мы отправим уведомление, если на вашей улице будет запланировано отключение света.")
                .build());

        return Event.REPLY_RECEIVED;
    }
}