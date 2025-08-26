package org.roxy.reminder.bot.dialogstatemachine.handlers.filloutcard;

import lombok.Builder;
import lombok.Data;
import org.roxy.reminder.bot.dialogstatemachine.enums.Event;
import org.roxy.reminder.bot.dialogstatemachine.enums.State;
import org.roxy.reminder.bot.dialogstatemachine.handlers.filloutcard.action.ActionHandler;

import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
public class StateDescriptor {
    public ActionHandler actionHandler;
    public ConcurrentHashMap<Event, State> transitions;
}
