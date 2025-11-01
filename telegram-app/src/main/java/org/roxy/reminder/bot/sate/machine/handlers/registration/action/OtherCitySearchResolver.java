package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.ButtonCallbackConstants;
import org.roxy.reminder.bot.service.UserSessionCacheService;
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
public class OtherCitySearchResolver extends ActionResolver {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private UserSessionCacheService userSessionCacheService;

    @Override
    public Event resolveAction(UpdateDto update) {
        final int MAX_CITIES_COUNT = 10;

        userSessionCacheService.clearCity(update.getChatId());
        String proposedCityName = update.getUserResponse();
        List<CityDto> cities = cityRepository.findWithFuzzySearchByName(proposedCityName);

        if (cities.isEmpty()) {
            super.botClient.sendMessage(
                    MessageDto.builder()
                            .chatId(String.valueOf(update.getChatId()))
                            .text("""
                            ❗Не удалось найти населенный пункт.
                             Пожалуйста, попробуйте ещё раз ❤️️️
                            """)
                            .build());
            return Event.RETRY;
        }

        String messageText;
        List<CityDto> citiesToDisplay = cities;
        if (cities.size() > MAX_CITIES_COUNT) {
            citiesToDisplay = cities.subList(0, MAX_CITIES_COUNT);
            messageText = """
                    ✅Мы нашли более 10 населенных пунктов. Показаны первые 10 результатов.
                    Если вашего населенного пункта нет в списке, уточните запрос.
                    
                    Пожалуйста, выберите населенный пункт из списка:""";
        } else {
            messageText = "✅Пожалуйста, выберите населенный пункт из списка";
        }

        InlineKeyboardDto.KeyboardBuilder keyboardCitiesBuilder = new InlineKeyboardDto.KeyboardBuilder();
        for (CityDto city : citiesToDisplay) {
            keyboardCitiesBuilder.addRow().addButton(city.getFullName(), city.getFiasId());
        }
        keyboardCitiesBuilder.addRow().addButton("Назад", ButtonCallbackConstants.BACK.name());
        InlineKeyboardDto keyboardCities = keyboardCitiesBuilder.build();

        super.botClient.sendMessage(
                MessageDto.builder()
                        .chatId(String.valueOf(update.getChatId()))
                        .text(messageText)
                        .replyMarkup(keyboardCities)
                        .build());

        return Event.REPLY_RECEIVED;
    }

    @Override
    public void sendActionWelcomeMessage(Long chatId) {
        super.botClient.sendMessage(
                MessageDto.builder()
                        .chatId(String.valueOf(chatId))
                        .text("Пожалуйста, введите имя населённого пункта")
                        .build());
    }
}