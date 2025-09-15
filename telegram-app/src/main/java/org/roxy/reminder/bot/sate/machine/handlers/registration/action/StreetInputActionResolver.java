package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.repository.StreetRepository;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.tgclient.dto.message.request.keyboard.InlineKeyboardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class StreetInputActionResolver extends ActionResolver {

    @Autowired
    private StreetRepository streetRepository;
    @Autowired
    private UserCartRepository userCartRepository;

    @Override
    public Event resolveAction(UpdateDto update) {
        log.info("Handling message = {}", update);
        Optional<UserCartEntity> userCart = userCartRepository.findByChatId(update.getChatId());
        if (userCart.isEmpty()) {
            throw new RuntimeException("User cart is empty for update = " + update);
        }
        List<String> streets = streetRepository.findWithFuzzySearchByCityIdAndName(
                userCart.get().getCity().getFiasId()
                , update.getUserResponse());

        if (streets.isEmpty()) {
            super.botClient.sendMessage(
                    MessageDto.builder()
                            .chatId(String.valueOf(update.getChatId()))
                            .text("Не удалось найти улицу. Может быть Вы ошиблись или у нас устаревший справочник :(")
                            .build());
            return Event.RETRY;

        }
        if (streets.size() > 10) {
            super.botClient.sendMessage(
                    MessageDto.builder()
                            .chatId(String.valueOf(update.getChatId()))
                            .text(String.format("Мы нашли более 10 улиц с текстом %s. Пожалуйста, уточните имя улицы", update.getUserResponse()))
                            .build());
            return Event.RETRY;
        }

        InlineKeyboardDto.KeyboardBuilder keyboardOfStreetsBuilder = new InlineKeyboardDto.KeyboardBuilder();
        for (String street : streets) {
            keyboardOfStreetsBuilder.addRow().addButton(street, street);
        }
        InlineKeyboardDto keyboardOfStreets = keyboardOfStreetsBuilder.build();

        super.botClient.sendMessage(
                MessageDto.builder()
                        .chatId(String.valueOf(update.getChatId()))
                        .text("Выберите улицу")
                        .replyMarkup(keyboardOfStreets)
                        .build());

        return Event.REPLY_RECEIVED;
    }
}