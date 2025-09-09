package org.roxy.reminder.bot.sate.machine.handlers.registration.action;

import lombok.Builder;
import lombok.Data;
import org.roxy.reminder.bot.sate.machine.enums.Event;

@Data
@Builder
public class ActionResult {
    private Event event;
}
