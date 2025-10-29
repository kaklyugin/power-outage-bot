package org.roxy.reminder.bot.sate.machine.handlers.registration;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.sate.machine.StateMachine;
import org.roxy.reminder.bot.sate.machine.StateMachineProvider;
import org.roxy.reminder.bot.sate.machine.enums.Event;
import org.roxy.reminder.bot.sate.machine.enums.State;
import org.roxy.reminder.bot.sate.machine.enums.StateMachineName;
import org.roxy.reminder.bot.sate.machine.handlers.UpdateHandler;
import org.roxy.reminder.bot.service.broker.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.StateMachineEntity;
import org.roxy.reminder.bot.persistence.repository.StateMachineRepository;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.roxy.reminder.bot.sate.machine.handlers.registration.action.*;
import org.roxy.reminder.bot.service.webclient.dto.updates.UpdateType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RegistrationHandler implements UpdateHandler {

    private final StateMachineRepository stateMachineRepository;
    private final UserCartRepository userCartRepository;
    private final StateMachineProvider stateMachineProvider;

    public RegistrationHandler(
            StateMachineRepository stateMachineRepository, UserCartRepository userCartRepository, StateMachineProvider stateMachineProvider
    ) {
        this.stateMachineRepository = stateMachineRepository;
        this.userCartRepository = userCartRepository;
        this.stateMachineProvider = stateMachineProvider;
    }

    @Override
    public boolean canHandle(UpdateDto update) {
        if (update.getUpdateType().equals(UpdateType.COMMAND)) {
            return true;
        } else {
            if (getStateMachineEntity(update).getState().equals(State.COMPLETED)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void handle(UpdateDto update) {

        if (update.getUserResponse().equals("/start")) {
            stateMachineRepository.findByChatId(update.getChatId())
                    .ifPresent(stateMachineRepository::delete);
            userCartRepository.findByChatId(update.getChatId())
                    .ifPresent(userCartRepository::delete);
        }
        StateMachineEntity stateMachineEntity = getStateMachineEntity(update);
        StateMachine stateMachine = stateMachineProvider.getStateMachine(update);
        ActionResolver actionResolver = stateMachine.getActionResolver(stateMachineEntity.getState());
        Event event = actionResolver.resolveAction(update);
        State nextState = stateMachine.getNextState(stateMachineEntity.getState(), event);
        stateMachineEntity.setState(nextState);
        stateMachine.getActionResolver(nextState).sendActionWelcomeMessage(update.getChatId());
        stateMachineRepository.save(stateMachineEntity);
    }

    private StateMachineEntity getStateMachineEntity(UpdateDto update) {
        return stateMachineRepository.findActiveByChatId(update.getChatId())
                .orElseGet(() -> stateMachineRepository.save(new StateMachineEntity(update.getChatId(), State.NEW, StateMachineName.REGISTRATION)));
    }
}