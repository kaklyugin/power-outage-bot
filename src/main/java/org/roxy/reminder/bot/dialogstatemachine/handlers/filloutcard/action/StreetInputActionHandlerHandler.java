package org.roxy.reminder.bot.dialogstatemachine.handlers.filloutcard.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.dialogstatemachine.enums.Event;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.DialogContextEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.StreetRepository;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.tgclient.dto.message.request.keyboard.InlineKeyboardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class StreetInputActionHandlerHandler implements ActionHandler {

    @Autowired
    private StreetRepository streetRepository;

    @Override
    public ActionResponseDto handleAction(UpdateDto update, UserCartEntity userCart)  {
        log.info("Handling message = {}", update);
        List<String> streets = streetRepository.findFuzzyByName(update.getUserResponse());
        if (streets.isEmpty()) {
            var response =
                    MessageDto.builder()
                            .chatId(String.valueOf(update.getChatId()))
                            .text("Не удалось найти улицу. Может быть Вы ошиблись или у нас устаревший справочник :(")
                            .build();
            return ActionResponseDto.builder()
                    .message(response)
                    .event(Event.RETRY)
                    .build();
        }
        if (streets.size() > 10) {
            var response =
                    MessageDto.builder()
                            .chatId(String.valueOf(update.getChatId()))
                            .text(String.format("Мы нашли более 10 улиц с текстом %s. Пожалуйста, уточните имя улицы", update.getUserResponse()))
                            .build();
            return ActionResponseDto.builder()
                    .message(response)
                    .event(Event.RETRY)
                    .build();
        }

        InlineKeyboardDto.KeyboardBuilder keyboardOfStreetsBuilder = new InlineKeyboardDto.KeyboardBuilder();
        for (String street : streets) {
            keyboardOfStreetsBuilder.addRow().addButton(street, street);
        }
        InlineKeyboardDto keyboardOfStreets = keyboardOfStreetsBuilder.build();

        var response =
                MessageDto.builder()
                        .chatId(String.valueOf(update.getChatId()))
                        .text("Выберите улицу")
                        .replyMarkup(keyboardOfStreets)
                        .build();

        return ActionResponseDto.builder()
                .message(response)
                .event(Event.REPLY_RECEIVED)
                .build();
    }
}