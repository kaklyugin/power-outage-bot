package org.roxy.reminder.bot.sate.machine;

import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.sate.machine.enums.State;
import org.roxy.reminder.bot.sate.machine.handlers.StateDescriptor;
import org.roxy.reminder.bot.sate.machine.handlers.registration.action.ActionResolver;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class StateMachine {
    protected final ConcurrentHashMap<State, StateDescriptor> states = new ConcurrentHashMap<>();

    public State getNextState(State state, Event currentEvent) {
        StateDescriptor currentState = states.get(state);
        Optional<State> nextState = Optional.ofNullable(currentState.getTransitions().get(currentEvent));
        return nextState.orElseThrow(() -> new RuntimeException("No transition found for " + state + " and " + currentEvent));
    }

    public ActionResolver getActionResolver(State state) {
        return states.get(state).getActionResolver();
    }
}
