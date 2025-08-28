package org.roxy.reminder.bot.dialogstatemachine.handlers.cartfillout.action;

import lombok.Builder;
import lombok.Data;
import org.roxy.reminder.bot.dialogstatemachine.enums.Event;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;

@Data
@Builder
public class ActionResponseDto {
    private MessageDto message;
    private Event event;
}
