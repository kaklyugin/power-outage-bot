package org.roxy.reminder.bot.sate.machine.handlers;

import org.roxy.reminder.bot.dto.UpdateDto;

public interface UpdateHandler {
     void handle(UpdateDto update);
     boolean canHandle(UpdateDto update);
}
