package org.roxy.reminder.bot.sate.machine.handlers.registration;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.sate.machine.StateMachine;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.sate.machine.enums.State;
import org.roxy.reminder.bot.sate.machine.handlers.StateDescriptor;
import org.roxy.reminder.bot.sate.machine.handlers.registration.action.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class RegistrationStateMachine extends StateMachine {

    public RegistrationStateMachine(StartMessageActionResolver startMessageActionHandle,
                                    CitySelectActionResolver citySelectActionHandle,
                                    SpecificCitySelectActionResolver specificCitySelectActionResolver,
                                    StreetInputActionResolver streetInputActionHandle,
                                    StreetSelectActionResolver streetSelectActionHandle) {
        super.states.put(State.NEW, StateDescriptor.builder()
                .actionResolver(startMessageActionHandle)
                .transitions(new ConcurrentHashMap<>() {{
                    put(Event.REPLY_RECEIVED, State.CITY_SELECT);
                }})
                .build());
        states.put(State.CITY_SELECT, StateDescriptor.builder()
                .actionResolver(citySelectActionHandle)
                .transitions(new ConcurrentHashMap<>() {{
                    put(Event.SPECIFIC_CITY_INPUT_REQUESTED, State.SPECIFIC_CITY_SELECT);
                    put(Event.REPLY_RECEIVED, State.STREET_INPUT);
                }})
                .build());
        states.put(State.SPECIFIC_CITY_SELECT, StateDescriptor.builder()
                .actionResolver(specificCitySelectActionResolver)
                .transitions(new ConcurrentHashMap<>() {{
                    put(Event.RETRY, State.SPECIFIC_CITY_SELECT);
                    put(Event.REPLY_RECEIVED, State.CITY_SELECT);
                }})
                .build());

        states.put(State.STREET_INPUT, StateDescriptor.builder()
                .actionResolver(streetInputActionHandle)
                .transitions(new ConcurrentHashMap<>() {{
                    put(Event.REPLY_RECEIVED, State.STREET_SELECT);
                    put(Event.RETRY, State.STREET_INPUT);
                }})
                .build());
        states.put(State.STREET_SELECT,
                StateDescriptor.builder()
                .actionResolver(streetSelectActionHandle)
                .transitions(new ConcurrentHashMap<>() {{
                    put(Event.REPLY_RECEIVED, State.COMPLETED);
                }})
                .build());
    }
}