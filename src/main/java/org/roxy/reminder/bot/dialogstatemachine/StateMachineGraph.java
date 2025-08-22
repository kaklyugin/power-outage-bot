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

    public StateMachineGraph(StartMessageHandler startMessageHandler,
                             CitySelectUpdateHandler citySelectUpdateHandler,
                             StreetInputUpdateHandler streetInputUpdateHandler) {

        StateDescriptor citySelect =
                StateDescriptor.builder()
                        .status(State.CITY_SELECT)
                        .handlers(
                                new ConcurrentHashMap<>() {{
                                    put(Event.REPLY_RECEIVED, citySelectUpdateHandler);
                                    put(Event.START_COMMAND_RECEIVED, startMessageHandler);
                                }})
                        .transitions(new ConcurrentHashMap<>() {{
                            put(Event.REPLY_RECEIVED, State.STREET_INPUT);
                            put(Event.START_COMMAND_RECEIVED, State.CITY_SELECT);
                        }})
                        .build();
        StateDescriptor streetInput =
                StateDescriptor.builder()
                        .status(State.STREET_INPUT)
                        .handlers(
                                new ConcurrentHashMap<>() {{
                                    put(Event.REPLY_RECEIVED, streetInputUpdateHandler);
                                    put(Event.START_COMMAND_RECEIVED, startMessageHandler);
                                }})
                        .transitions(new ConcurrentHashMap<>() {{
                            put(Event.REPLY_RECEIVED, State.COMPLETED);
                            put(Event.START_COMMAND_RECEIVED, State.CITY_SELECT);
                        }})
                        .build();
        StateDescriptor completed =
                StateDescriptor.builder()
                        .status(State.COMPLETED)
                        .handlers(
                                new ConcurrentHashMap<>() {{
                                    put(Event.START_COMMAND_RECEIVED, startMessageHandler);
                                }})
                        .transitions(new ConcurrentHashMap<>() {{
                            put(Event.START_COMMAND_RECEIVED, State.CITY_SELECT);
                        }})
                        .build();

        states.put(State.CITY_SELECT, citySelect);
        states.put(State.STREET_INPUT, streetInput);
        states.put(State.COMPLETED, completed);
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
