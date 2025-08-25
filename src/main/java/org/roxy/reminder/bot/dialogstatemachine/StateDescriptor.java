package org.roxy.reminder.bot.dialogstatemachine;

import lombok.Builder;
import lombok.Data;
import org.roxy.reminder.bot.dialogstatemachine.enums.Event;
import org.roxy.reminder.bot.dialogstatemachine.enums.State;
import org.roxy.reminder.bot.dialogstatemachine.handlers.UpdateHandler;

import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
public class StateDescriptor {
    private State state;
    public UpdateHandler handler;
    public ConcurrentHashMap<Event, State> transitions;
}
