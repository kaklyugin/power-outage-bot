package org.roxy.reminder.bot.sate.machine.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.sate.machine.StateMachine;
import org.roxy.reminder.bot.sate.machine.handlers.registration.RegistrationStateMachine;

@Getter
@Slf4j
public enum StateMachineName {
    REGISTRATION(RegistrationStateMachine.class);

    private final Class<? extends StateMachine> stateMachineClass;

    StateMachineName(Class<? extends StateMachine> stateMachineClass) {
        this.stateMachineClass = stateMachineClass;
    }
}
