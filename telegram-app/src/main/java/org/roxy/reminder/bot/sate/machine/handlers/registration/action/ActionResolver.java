package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.service.http.BotClient;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class ActionResolver {
    @Autowired
    protected BotClient botClient;
    public abstract Event resolveAction(UpdateDto update);
}
