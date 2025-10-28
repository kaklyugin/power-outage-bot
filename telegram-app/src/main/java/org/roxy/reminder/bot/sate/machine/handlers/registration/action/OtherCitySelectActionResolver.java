package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.ButtonCallbackConstants;
import org.roxy.reminder.bot.service.broker.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.dto.CityDto;
import org.roxy.reminder.bot.persistence.repository.CityRepository;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.service.webclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.service.webclient.dto.message.request.keyboard.InlineKeyboardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class OtherCitySelectActionResolver extends ActionResolver {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public Event resolveAction(UpdateDto update) {

        String proposedCityName = update.getUserResponse();

        List<CityDto> cities = cityRepository.findWithFuzzySearchByName(proposedCityName);

        if (cities.isEmpty()) {
            super.botClient.sendMessage(
                    MessageDto.builder()
                            .chatId(String.valueOf(update.getChatId()))
                            .text("Не удалось найти населенный пункт. Может быть Вы ошиблись или у нас устаревший справочник :(")
                            .build());
            return Event.RETRY;

        }
        if (cities.size() > 10) {
            super.botClient.sendMessage(
                    MessageDto.builder()
                            .chatId(String.valueOf(update.getChatId()))
                            .text(String.format("Мы нашли более 10 населенных пунктов с текстом %s. Пожалуйста, уточните наименование", update.getUserResponse()))
                            .build());
            return Event.RETRY;
        }

        InlineKeyboardDto.KeyboardBuilder keyboardCitiesBuilder = new InlineKeyboardDto.KeyboardBuilder();
        for (CityDto city : cities) {
            keyboardCitiesBuilder.addRow().addButton(city.getName(), city.getFiasId());
        }
        keyboardCitiesBuilder.addRow().addButton("Назад", ButtonCallbackConstants.BACK.name());
        InlineKeyboardDto keyboardCities = keyboardCitiesBuilder.build();

        super.botClient.sendMessage(
                MessageDto.builder()
                        .chatId(String.valueOf(update.getChatId()))
                        .text("Выберите населенный пункт из списка")
                        .replyMarkup(keyboardCities)
                        .build());

        return Event.REPLY_RECEIVED;

    }
}