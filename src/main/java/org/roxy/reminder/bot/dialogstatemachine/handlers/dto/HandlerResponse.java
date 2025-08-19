package org.roxy.reminder.bot.dialogstatemachine.handlers.dto;

import lombok.Builder;
import lombok.Data;
import org.roxy.reminder.bot.dialogstatemachine.enums.Event;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;

@Data
@Builder
public class HandlerResponse {
    private Event event;
    private MessageDto message;
}
