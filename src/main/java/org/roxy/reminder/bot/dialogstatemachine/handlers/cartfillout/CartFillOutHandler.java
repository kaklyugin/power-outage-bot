package org.roxy.reminder.bot.dialogstatemachine.handlers.cartfillout;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.dialogstatemachine.enums.Event;
import org.roxy.reminder.bot.dialogstatemachine.enums.State;
import org.roxy.reminder.bot.dialogstatemachine.handlers.StateDescriptor;
import org.roxy.reminder.bot.dialogstatemachine.handlers.UpdateHandler;
import org.roxy.reminder.bot.dialogstatemachine.handlers.cartfillout.action.*;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.StateMachineEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.StateMachineRepository;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.roxy.reminder.bot.tgclient.dto.message.response.SendMessageResponseDto;
import org.roxy.reminder.bot.tgclient.dto.updates.UpdateType;
import org.roxy.reminder.bot.service.http.HttpBotClient;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class CartFillOutHandler implements UpdateHandler {

    private final StateMachineRepository stateMachineRepository;
    private final UserCartRepository userCartRepository;
    private final HttpBotClient botClient;
    private final ConcurrentHashMap<State, StateDescriptor> states = new ConcurrentHashMap<>();
    private StateMachineEntity currentState;

    public CartFillOutHandler(StartMessageActionHandlerHandler startMessageActionHandle,
                              CitySelectActionHandlerHandler citySelectActionHandle,
                              StreetInputActionHandlerHandler streetInputActionHandle,
                              StreetSelectActionHandlerHandler streetSelectActionHandle,
                              StateMachineRepository stateMachineRepository,
                              UserCartRepository userCartRepository,
                              HttpBotClient botClient) {
        this.stateMachineRepository = stateMachineRepository;
        this.userCartRepository = userCartRepository;
        this.botClient = botClient;

        states.put(State.NEW, StateDescriptor.builder()
                .actionHandler(startMessageActionHandle)
                .transitions(new ConcurrentHashMap<>() {{
                    put(Event.REPLY_RECEIVED, State.CITY_SELECT);
                }})
                .build());
        states.put(State.CITY_SELECT, StateDescriptor.builder()
                .actionHandler(citySelectActionHandle)
                .transitions(new ConcurrentHashMap<>() {{
                    put(Event.REPLY_RECEIVED, State.STREET_INPUT);
                }})
                .build());
        states.put(State.STREET_INPUT, StateDescriptor.builder()
                .actionHandler(streetInputActionHandle)
                .transitions(new ConcurrentHashMap<>() {{
                    put(Event.REPLY_RECEIVED, State.STREET_SELECT);
                    put(Event.RETRY, State.STREET_INPUT);
                }})
                .build());

        states.put(State.STREET_SELECT,
                StateDescriptor.builder()
                        .actionHandler(streetSelectActionHandle)
                        .transitions(new ConcurrentHashMap<>() {{
                            put(Event.REPLY_RECEIVED, State.COMPLETED);
                        }})
                        .build());
    }

    @Override
    public void handle(UpdateDto update) {
        try {
            if (update.getUpdateType().equals(UpdateType.COMMAND)) {
                handleCommand(update);
            } else {
                handleUserAction(update);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean canHandle(UpdateDto update) {
        if (update.getUpdateType().equals(UpdateType.COMMAND)) {
            return true;
        }
        else
        {
            if (getCurrentState(update).getState().equals(State.COMPLETED)) {
                return false;
            }
        }
        return true;
    }

    private void handleCommand(UpdateDto update) {
        {
            if (update.getUserResponse().equals("/start")) {
                stateMachineRepository.deleteById(update.getChatId());
                userCartRepository.deleteById(update.getChatId());
                handleUserAction(update);
            }
        }
    }

    private void handleUserAction(UpdateDto update) {
        if (this.currentState == null) {
            this.currentState = getCurrentState(update);
        }
        UserCartEntity userCart = getUserCart(update);
        ActionHandler actionHandler = getActionHandler(currentState.getState());
        ActionResponseDto actionResponseDto = actionHandler.handleAction(update, userCart);
        if (actionResponseDto.getMessage() != null) {
            SendMessageResponseDto messageResponseDto = botClient.sendMessage(actionResponseDto.getMessage());
            if (messageResponseDto.isOk()) {
                currentState.setLastBotMessageId(messageResponseDto.getMessageId());
                stateMachineRepository.save(currentState);
            } else throw new RuntimeException("Could not send message " + actionResponseDto.getMessage());
        }
        currentState.setState(getNextState(currentState.getState(), Event.REPLY_RECEIVED));
        stateMachineRepository.save(currentState);
    }
    // TODO Вопрос : может лучше сразу присвоить this.currentState ?
    private StateMachineEntity getCurrentState(UpdateDto update) {
        return stateMachineRepository.findById(update.getChatId())
                .orElse(new StateMachineEntity(update.getChatId(), State.NEW));
    }

    private UserCartEntity getUserCart(UpdateDto update) {
        return userCartRepository
                .findByChatId(update.getChatId())
                .orElse(new UserCartEntity(update.getChatId()));
    }

    private State getNextState(State state, Event currentEvent) {
        StateDescriptor currentState = states.get(state);
        Optional<State> nextState = Optional.ofNullable(currentState.getTransitions().get(currentEvent));
        return nextState.orElseThrow(() -> new RuntimeException("No transition found for " + state + " and " + currentEvent));
    }

    private ActionHandler getActionHandler(State state) {
        return states.get(state).getActionHandler();
    }
}