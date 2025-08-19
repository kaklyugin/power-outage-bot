package org.roxy.reminder.bot.dialogstatemachine.handlers;

import org.roxy.reminder.bot.dialogstatemachine.handlers.dto.HandlerResponse;
import org.roxy.reminder.bot.persistence.entity.DialogContextEntity;
import org.roxy.reminder.bot.dto.UpdateDto;

public interface UpdateHandler {
    HandlerResponse handleUpdate(UpdateDto update, DialogContextEntity context);
}
