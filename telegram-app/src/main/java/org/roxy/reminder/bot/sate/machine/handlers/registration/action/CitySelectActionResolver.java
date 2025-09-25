package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.Constantst;
import org.roxy.reminder.bot.persistence.entity.CityEntity;
import org.roxy.reminder.bot.persistence.repository.CityRepository;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.service.UserCartService;
import org.roxy.reminder.bot.service.broker.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.roxy.reminder.bot.service.webclient.dto.message.request.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CitySelectActionResolver extends ActionResolver {

    @Autowired
    private UserCartService userCartService;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public Event resolveAction(UpdateDto update) {

        if (update.getUserResponse().equals(Constantst.OTHER_CITY))
        {
            super.botClient.sendMessage(MessageDto.builder()
                    .chatId(String.valueOf(update.getChatId()))
                    .text("Введите название населенного пункта ")
                    .build());
            return Event.SPECIFIC_CITY_INPUT_REQUESTED;
        }

        log.info("Handling update = {}", update);
        UserCartEntity userCart = userCartService.getUserCartByChatId(update.getChatId());
        CityEntity cityEntity = cityRepository.findById(update.getUserResponse())
                .orElseThrow(() -> new RuntimeException(
                String.format("Failed to save user response. City with id = %s not found", update.getUserResponse())));
        userCartService.addCity(userCart.getId(),cityEntity.getFiasId());
        userCartService.save(userCart);
        super.botClient.sendMessage(MessageDto.builder()
                .chatId(String.valueOf(update.getChatId()))
                .text("Введите улицу")
                .build());
        return Event.REPLY_RECEIVED;
    }
}