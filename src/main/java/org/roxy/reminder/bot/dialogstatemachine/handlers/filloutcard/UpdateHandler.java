package org.roxy.reminder.bot.dialogstatemachine.handlers.filloutcard;

import org.roxy.reminder.bot.dto.UpdateDto;

public interface UpdateHandler {
     void handle(UpdateDto update);
     boolean canHandle(UpdateDto update);
}
