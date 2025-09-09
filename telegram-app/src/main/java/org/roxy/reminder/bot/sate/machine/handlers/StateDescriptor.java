package org.roxy.reminder.bot.sate.machine.handlers;

import lombok.Builder;
import lombok.Data;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.sate.machine.enums.State;
import org.roxy.reminder.bot.sate.machine.handlers.registration.action.ActionResolver;

import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
public class StateDescriptor {
    public ActionResolver actionResolver;
    public ConcurrentHashMap<Event, State> transitions;
}
