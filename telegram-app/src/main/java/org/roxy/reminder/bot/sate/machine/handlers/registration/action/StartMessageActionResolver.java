package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.persistence.entity.CityEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.CityRepository;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.tgclient.dto.message.request.keyboard.InlineKeyboardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class StartMessageActionResolver extends ActionResolver {
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private UserCartRepository userCartRepository;

    @Override
    public Event resolveAction(UpdateDto update) {
        log.info("Handling message = {}", update);
        userCartRepository.save(
                UserCartEntity.builder()
                        .chatId(update.getChatId())
                        .build());
        List<CityEntity> cities = cityRepository.findAll();
        var keyboardBuilder = new InlineKeyboardDto.KeyboardBuilder();
        for (CityEntity city : cities) {
            keyboardBuilder.addRow().addButton(city.getName(), city.getFiasId());
        }
        var citiesKeyboard = keyboardBuilder.build();

        MessageDto citySelectMessage =
                MessageDto.builder()
                        .chatId(String.valueOf(update.getChatId()))
                        .text("Выберите город")
                        .replyMarkup(citiesKeyboard)
                        .build();

        super.botClient.sendMessage(citySelectMessage);
        return Event.REPLY_RECEIVED;
    }
}
