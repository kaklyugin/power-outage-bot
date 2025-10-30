package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.ButtonCallbackConstants;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.service.UserSessionCacheService;
import org.roxy.reminder.bot.service.broker.dto.UpdateDto;
import org.roxy.reminder.bot.service.suggestion.LocationDto;
import org.roxy.reminder.bot.service.suggestion.SuggestionService;
import org.roxy.reminder.bot.service.webclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.service.webclient.dto.message.request.keyboard.InlineKeyboardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class StreetInputActionResolver extends ActionResolver {

    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private UserSessionCacheService userSessionCacheService;


    @Override
    public Event resolveAction(UpdateDto update) {
        log.info("Handling message = {}", update);

        if(update.getUserResponse().equals(ButtonCallbackConstants.BACK.name())) {
            return Event.BACK;
        }

        List<LocationDto> streets = suggestionService.getStreetSuggestions(
                userSessionCacheService.findByChatId(update.getChatId()).getUserSessionData().getCityFiasId(),
                update.getUserResponse());

        if (streets.isEmpty()) {
            super.botClient.sendMessage(
                    MessageDto.builder()
                            .chatId(String.valueOf(update.getChatId()))
                            .text("❗Не удалось найти улицу. Попробуйте, пожалуйста, ввести имя улицы ещё раз")
                            .build());
            return Event.RETRY;

        }
        if (streets.size() > 10) {
            super.botClient.sendMessage(
                    MessageDto.builder()
                            .chatId(String.valueOf(update.getChatId()))
                            .text(String.format("❗Мы нашли более 10 улиц с текстом %s. Пожалуйста, уточните имя улицы", update.getUserResponse()))
                            .build());
            return Event.RETRY;
        }

        InlineKeyboardDto.KeyboardBuilder keyboardStreetsBuilder = new InlineKeyboardDto.KeyboardBuilder();
        for (LocationDto locationDto : streets) {
            keyboardStreetsBuilder.addRow().addButton(locationDto.getLocationFullName(), locationDto.getLocationFiasId());
        }
        keyboardStreetsBuilder.addRow().addButton("Назад", ButtonCallbackConstants.BACK.name());
        InlineKeyboardDto keyboardStreets = keyboardStreetsBuilder.build();

        super.botClient.sendMessage(
                MessageDto.builder()
                        .chatId(String.valueOf(update.getChatId()))
                        .text("✅ Пожалуйста, выберите улицу из списка")
                        .replyMarkup(keyboardStreets)
                        .build());

        return Event.REPLY_RECEIVED;
    }

    @Override
    public void sendActionWelcomeMessage(Long chatId) {
        super.botClient.sendMessage(
                MessageDto.builder()
                        .chatId(String.valueOf(chatId))
                        .text("✅ Пожалуйста, введите имя  улицы")
                        .build());
    }
}