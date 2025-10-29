package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.ButtonCallbackConstants;
import org.roxy.reminder.bot.persistence.entity.CityEntity;
import org.roxy.reminder.bot.persistence.entity.UserSessionCacheEntity;
import org.roxy.reminder.bot.persistence.repository.CityRepository;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.service.UserCartService;
import org.roxy.reminder.bot.service.UserSessionCacheService;
import org.roxy.reminder.bot.service.broker.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.service.webclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.service.webclient.dto.message.request.keyboard.InlineKeyboardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CitySelectActionResolver extends ActionResolver {

    @Autowired
    private UserSessionCacheService userSessionCacheService;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public Event resolveAction(UpdateDto update) {
        userSessionCacheService.clearCity(update.getChatId());

        if (update.getUserResponse().equals(ButtonCallbackConstants.OTHER_CITY.name())) {
            return Event.SPECIFIC_CITY_INPUT_REQUESTED;
        }

        log.info("Handling update = {}", update);

        cityRepository.findById(update.getUserResponse())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Failed to save user response. Could not find city with fiasId = " + update.getUserResponse()));
        UserSessionCacheEntity userSessionEntity = userSessionCacheService.findByChatId(update.getChatId());
        userSessionEntity.getUserSessionData().setCityFiasId(update.getUserResponse());
        userSessionCacheService.save(userSessionEntity);

        return Event.REPLY_RECEIVED;
    }

    @Override
    public void sendActionWelcomeMessage(Long chatId) {
        InlineKeyboardDto citiesKeyboard = createKeyboardWithCities();
        MessageDto citySelectMessage =
                MessageDto.builder()
                        .chatId(String.valueOf(chatId))
                        .text("Выберите город из списка")
                        .replyMarkup(citiesKeyboard)
                        .build();
        super.botClient.sendMessage(citySelectMessage);

    }

    private InlineKeyboardDto createKeyboardWithCities() {
        List<CityEntity> cities = cityRepository.findTopMenuDefaultCities();
        var keyboardBuilder = new InlineKeyboardDto.KeyboardBuilder();
        for (CityEntity city : cities) {
            keyboardBuilder.addRow().addButton(city.getName(), city.getFiasId());
        }
        keyboardBuilder.addRow().addButton("Другой...", ButtonCallbackConstants.OTHER_CITY.name());
        return keyboardBuilder.build();
    }
}