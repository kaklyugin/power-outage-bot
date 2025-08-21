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
                        .handlers(
                                new ConcurrentHashMap<>() {{
                                    put(Event.START_COMMAND_RECEIVED, startMessageHandler);
                                }})
                        .transitions(new ConcurrentHashMap<>() {{
                            put(Event.START_COMMAND_RECEIVED, State.WAITING_FOR_CITY_INPUT);
                        }})
                        .build();
        StateDescriptor waitingForCityInputState =
                StateDescriptor.builder()
                        .status(State.WAITING_FOR_CITY_INPUT)
                        .handlers(
                                new ConcurrentHashMap<>() {{
                                    put(Event.REPLY_RECEIVED, citySelectUpdateHandler);
                                    put(Event.START_COMMAND_RECEIVED, startMessageHandler);
                                }})
                        .transitions(new ConcurrentHashMap<>() {{
                            put(Event.REPLY_RECEIVED, State.WAITING_FOR_STREET_INPUT);
                            put(Event.START_COMMAND_RECEIVED, State.NEW);
                        }})
                        .build();
        StateDescriptor waitingForStreetInputState =
                StateDescriptor.builder()
                        .status(State.WAITING_FOR_STREET_INPUT)
                        .handlers(
                                new ConcurrentHashMap<>() {{
                                    put(Event.REPLY_RECEIVED, streetInputUpdateHandler);
                                    put(Event.START_COMMAND_RECEIVED, startMessageHandler);
                                }})
                        .transitions(new ConcurrentHashMap<>() {{
                            put(Event.REPLY_RECEIVED, State.DONE);
                            put(Event.START_COMMAND_RECEIVED, State.NEW);
                        }})
                        .build();

        states.put(State.NEW, newState);
        states.put(State.WAITING_FOR_CITY_INPUT, waitingForCityInputState);
        states.put(State.WAITING_FOR_STREET_INPUT, waitingForStreetInputState);
    }

    public State getNextState(State state, Event currentEvent) {
        StateDescriptor currentState = states.get(state);
        Optional<State> nextState = Optional.ofNullable(currentState.getTransitions().get(currentEvent));
        return nextState.orElseThrow(() -> new RuntimeException("No transition found for " + state + " and " + currentEvent));
    }

    public UpdateHandler getHandler(State state, Event event) {
        return states.get(state).handlers.get(event);
    }
}
