package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.ButtonCallbackConstants;
import org.roxy.reminder.bot.service.UserCartService;
import org.roxy.reminder.bot.service.broker.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.service.webclient.dto.message.request.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StreetSelectActionResolver extends ActionResolver {

    @Autowired
    private UserCartService userCartService;

    private final String SUCCESS_REGISTRATION_TEXT = """
            🎉 Отлично!
            ✅ Всё настроено.
            Теперь мы будем присылать вам уведомления о плановых отключениях света на вашей улице 🔔
            Как только появятся новые данные - мы обязательно вам сообщим❤️
            P.S.
            Если вы захотите изменить город или улицу, запустите бот заново из меню командой /start""";

    private final String ERROR_MESSAGE_TEXT = """
            ❗ Похоже, что вы ввели неправильный адрес.
            Пожалуйста, попробуйте ещё раз ввести имя улицы.""";

    @Override
    public Event resolveAction(UpdateDto update) {

        if(update.getUserResponse().equals(ButtonCallbackConstants.BACK.name())) {
            return Event.BACK;
        }

        log.info("Handling message = {}", update);
        String locationFiasId = update.getUserResponse();
        Long chatId = update.getChatId();
        UserCartEntity userCart = userCartService.getUserCartByChatId(chatId);
        if (!userCartService.checkLocationExists(locationFiasId)) {
            super.botClient.sendMessage(MessageDto.builder()
                    .chatId(String.valueOf(chatId))
                    .text(ERROR_MESSAGE_TEXT)
                    .build());
            return Event.RETRY;
        }
        userCartService.addLocation(userCart.getId(), locationFiasId);
        super.botClient.sendMessage(MessageDto.builder()
                .chatId(String.valueOf(chatId))
                .text(SUCCESS_REGISTRATION_TEXT)
                .build());
        return Event.REPLY_RECEIVED;
    }
}