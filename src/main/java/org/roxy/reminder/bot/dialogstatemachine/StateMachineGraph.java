package org.roxy.reminder.bot.dialogstatemachine;

import org.roxy.reminder.bot.dialogstatemachine.enums.Event;
import org.roxy.reminder.bot.dialogstatemachine.enums.State;
import org.roxy.reminder.bot.dialogstatemachine.handlers.CitySelectUpdateHandler;
import org.roxy.reminder.bot.dialogstatemachine.handlers.StartMessageHandler;
import org.roxy.reminder.bot.dialogstatemachine.handlers.StreetInputUpdateHandler;
import org.roxy.reminder.bot.dialogstatemachine.handlers.UpdateHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StateMachineGraph {

    private final ConcurrentHashMap<State, StateDescriptor> states = new ConcurrentHashMap<>();
    private final StartMessageHandler startMessageHandler;
    private final CitySelectUpdateHandler citySelectUpdateHandler;
    private final StreetInputUpdateHandler streetInputUpdateHandler;


    public StateMachineGraph(StartMessageHandler startMessageHandler, CitySelectUpdateHandler citySelectUpdateHandler, StreetInputUpdateHandler streetInputUpdateHandler) {
        this.startMessageHandler = startMessageHandler;
        this.citySelectUpdateHandler = citySelectUpdateHandler;
        this.streetInputUpdateHandler = streetInputUpdateHandler;

        StateDescriptor newState =
                StateDescriptor.builder()
                        .status(State.NEW)
                        .handler(startMessageHandler)
                        .allowedTransitions(new ConcurrentHashMap<>() {{
                            put(Event.SEND_TEXT, State.WAITING_FOR_CITY_INPUT);
                        }})
                        .build();
        StateDescriptor waitingForCityInput =
                StateDescriptor.builder()
                        .status(State.WAITING_FOR_CITY_INPUT)
                        .handler(citySelectUpdateHandler)
                        .allowedTransitions(new ConcurrentHashMap<>() {{
                            put(Event.REPLY_RECEIVED, State.WAITING_FOR_STREET_INPUT);
                        }})
                        .build();
        StateDescriptor waitingForStreetInput =
                StateDescriptor.builder()
                        .status(State.WAITING_FOR_STREET_INPUT)
                        .handler(streetInputUpdateHandler)
                        .allowedTransitions(new ConcurrentHashMap<>() {{
                            put(Event.REPLY_RECEIVED, State.DONE);
                        }})
                        .build();
        states.put(State.NEW, newState);
        states.put(State.WAITING_FOR_CITY_INPUT, waitingForCityInput);
        states.put(State.WAITING_FOR_STREET_INPUT, waitingForStreetInput);

    }

    public State getNextState(State state, Event currentEvent) {
        StateDescriptor currentState = states.get(state);
        Optional<State> nextState = Optional.ofNullable(currentState.getAllowedTransitions().get(currentEvent));
        return nextState.orElseThrow(() -> new RuntimeException("No transition found for " + state + " and " + currentEvent));
    }

    public UpdateHandler getHandler(State state) {
        return states.get(state).getHandler();
    }
}
