package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.ButtonCallbackConstants;
import org.roxy.reminder.bot.persistence.entity.CityEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.CityRepository;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.service.UserCartService;
import org.roxy.reminder.bot.service.broker.dto.UpdateDto;
import org.roxy.reminder.bot.service.webclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.service.webclient.dto.message.request.keyboard.InlineKeyboardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class StartMessageActionResolver extends ActionResolver {
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private UserCartService userCartService;

    public static final String WELCOME_MESSAGE = """
    🙌 Здравствуйте
    Несколько слов о нашем сервисе💡

    Этот бот создан для того, чтобы заранее уведомлять вас о плановых отключениях электроэнергии.

    Как это работает:
    - Вы получите уведомление 🔔 только в том случае, если на вашей улице запланированы аварийные работы
    - Данные об отключениях мы получаем с официального сайта https://donenergo.ru/

    Важно знать:
    🆓 Это некоммерческий проект, который не принадлежит компании «Донэнерго»
    🙅‍♂️ Мы не несём ответственность за точность предоставляемой информации
    ⚡️ Стараемся быть максимально оперативными и достоверными
    ❤️️️ Не храним ваши персональные данные и номера телефонов

    Чтобы начать пользоваться сервисом, выберите город, в котором проживаете:

    ✅ Нажмите на кнопку выбора города ниже""";

    @Override
    public Event resolveAction(UpdateDto update) {
        log.info("Handling message = {}", update);
        userCartService.save(
                UserCartEntity.builder()
                        .chatId(update.getChatId())
                        .username(update.getUsername())
                        .build());
        List<CityEntity> cities = cityRepository.findTopMenuDefaultCities();
        var keyboardBuilder = new InlineKeyboardDto.KeyboardBuilder();
        for (CityEntity city : cities) {
            keyboardBuilder.addRow().addButton(city.getName(), city.getFiasId());
        }
        keyboardBuilder.addRow().addButton("Другой...", ButtonCallbackConstants.OTHER_CITY.name());
        var citiesKeyboard = keyboardBuilder.build();

        MessageDto citySelectMessage =
                MessageDto.builder()
                        .chatId(String.valueOf(update.getChatId()))
                        .text(WELCOME_MESSAGE)
                        .replyMarkup(citiesKeyboard)
                        .build();

        super.botClient.sendMessage(citySelectMessage);
        return Event.REPLY_RECEIVED;
    }
}
