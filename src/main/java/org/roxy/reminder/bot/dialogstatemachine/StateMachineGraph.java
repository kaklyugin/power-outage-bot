package org.roxy.reminder.bot.dialogstatemachine;

import org.roxy.reminder.bot.dialogstatemachine.enums.Event;
import org.roxy.reminder.bot.dialogstatemachine.enums.State;
import org.roxy.reminder.bot.dialogstatemachine.handlers.*;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StateMachineGraph {

    private final ConcurrentHashMap<State, StateDescriptor> states = new ConcurrentHashMap<>();

    public StateMachineGraph(StartMessageHandler startMessageHandler,
                             CitySelectHandler citySelectHandler,
                             StreetInputUpdateHandler streetInputUpdateHandler,
                             StreetSelectHandler streetSelectHandler) {

        StateDescriptor newState =
                StateDescriptor.builder()
                        .state(State.NEW)
                        .handler(startMessageHandler)
                        .transitions(new ConcurrentHashMap<>() {{
                            put(Event.REPLY_RECEIVED, State.CITY_SELECT);
                        }})
                        .build();

        StateDescriptor citySelect =
                StateDescriptor.builder()
                        .state(State.CITY_SELECT)
                        .handler(citySelectHandler)
                        .transitions(new ConcurrentHashMap<>() {{
                            put(Event.REPLY_RECEIVED, State.STREET_INPUT);
                        }})
                        .build();

        StateDescriptor streetInput =
                StateDescriptor.builder()
                        .state(State.STREET_INPUT)
                        .handler(streetInputUpdateHandler)
                        .transitions(new ConcurrentHashMap<>() {{
                            put(Event.REPLY_RECEIVED, State.STREET_SELECT);
                            put(Event.RETRY, State.STREET_INPUT);
                        }})
                        .build();

        StateDescriptor streetSelect =
                StateDescriptor.builder()
                        .state(State.STREET_SELECT)
                        .handler(streetSelectHandler)
                        .transitions(new ConcurrentHashMap<>() {{
                            put(Event.REPLY_RECEIVED, State.COMPLETED);
                        }})
                        .build();

        states.put(State.NEW, newState);
        states.put(State.CITY_SELECT, citySelect);
        states.put(State.STREET_INPUT, streetInput);
        states.put(State.STREET_SELECT, streetSelect);

    }

    public State getNextState(State state, Event currentEvent) {
        StateDescriptor currentState = states.get(state);
        Optional<State> nextState = Optional.ofNullable(currentState.getTransitions().get(currentEvent));
        return nextState.orElseThrow(() -> new RuntimeException("No transition found for " + state + " and " + currentEvent));
    }

    public UpdateHandler getHandler(State state) {
        return states.get(state).getHandler();
    }
}
