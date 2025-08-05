package org.roxy.reminder.bot.dialogstatemachine.handlers;

import org.roxy.reminder.bot.dialogstatemachine.storage.ChatContext;
import org.roxy.reminder.bot.dto.UpdateDto;

public interface UpdateHandler {
    boolean isApplicable(UpdateDto update, ChatContext context);

    void handleUpdate(UpdateDto update, ChatContext context);
}
