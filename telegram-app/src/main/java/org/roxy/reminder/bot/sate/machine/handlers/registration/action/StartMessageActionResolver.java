package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.Constantst;
import org.roxy.reminder.bot.persistence.entity.CityEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.CityRepository;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
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

    // FIXME get from table repository - set isDefault attribute
    private final String RASSVET = "35e9349a-c1bc-435a-86e3-0bb2fc2d7ed5";
    private final String NOVOCHERKASSK = "28bafcb3-92b2-445b-9443-a341be73fdb9";
    private final String ROSTOV = "c1cfe4b9-f7c2-423c-abfa-6ed1c05a15c5";
    private final String AKSAY = "c1cfe4b9-f7c2-423c-abfa-6ed1c05a15c5";
    private final String AZOV = "a216cad5-7027-40b8-b1a1-d64abefbd5cd";
    private final String SHAKHTY = "dee2e80e-f2e1-4a68-93b0-b7b89b6f3e74";

    private final List<String> PROPOSED_CITIES = List.of(
            RASSVET,
            NOVOCHERKASSK,
            ROSTOV,
            AKSAY,
            AZOV,
            SHAKHTY
            );

    @Override
    public Event resolveAction(UpdateDto update) {
        log.info("Handling message = {}", update);
        userCartService.save(
                UserCartEntity.builder()
                        .chatId(update.getChatId())
                        .build());
        List<CityEntity> cities = cityRepository.findByFiasIdsIn(PROPOSED_CITIES);
        var keyboardBuilder = new InlineKeyboardDto.KeyboardBuilder();
        for (CityEntity city : cities) {
            keyboardBuilder.addRow().addButton(city.getName(), city.getFiasId());
        }
        keyboardBuilder.addRow().addButton("Другой...", Constantst.OTHER_CITY);
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
