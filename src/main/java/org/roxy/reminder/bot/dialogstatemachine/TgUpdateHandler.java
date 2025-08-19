package org.roxy.reminder.bot.dialogstatemachine;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.dialogstatemachine.enums.State;
import org.roxy.reminder.bot.dialogstatemachine.handlers.dto.HandlerResponse;
import org.roxy.reminder.bot.dialogstatemachine.handlers.UpdateHandler;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.DialogContextEntity;
import org.roxy.reminder.bot.persistence.entity.StateMachineEntity;
import org.roxy.reminder.bot.persistence.repository.DialogContextRepository;
import org.roxy.reminder.bot.persistence.repository.StateMachineRepository;
import org.roxy.reminder.bot.tgclient.dto.message.response.SendMessageResponseDto;
import org.roxy.reminder.bot.tgclient.service.http.BotClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TgUpdateHandler {

    private final StateMachineGraph stateMachineGraph;
    private final StateMachineRepository stateMachineRepository;
    private final DialogContextRepository dialogContextRepository;
    private final BotClient botClient;

    public TgUpdateHandler(StateMachineRepository stateMachineRepository, StateMachineGraph stateMachineGraph, BotClient botClient, DialogContextRepository dialogContextRepository) {
        this.stateMachineRepository = stateMachineRepository;
        this.stateMachineGraph = stateMachineGraph;
        this.dialogContextRepository = dialogContextRepository;
        this.botClient = botClient;
    }


    public void handle(UpdateDto update) {
        try {
            StateMachineEntity currentState = stateMachineRepository.findById(update.getChatId())
                    .orElse(new StateMachineEntity(update.getChatId()));
            DialogContextEntity dialogContext = dialogContextRepository.findById(update.getChatId()).orElse(new DialogContextEntity(update.getChatId()));
            //FIXME  newInstance() is deprecated
            UpdateHandler updateHandler = stateMachineGraph.getHandler(currentState.getState());
            HandlerResponse handlerResponse = updateHandler.handleUpdate(update, dialogContext);
            // TODO Подумать над retry логикой отправки сообщений

            if (handlerResponse.getMessage() != null) {
                SendMessageResponseDto messageResponseDto = botClient.sendMessage(handlerResponse.getMessage());
                if (messageResponseDto.isOk()) {
                    dialogContext.setLastBotMessageId(messageResponseDto.getMessageId());
                    dialogContextRepository.save(dialogContext);
                } else throw new RuntimeException("Could not send message " + handlerResponse.getMessage());
            }
            State newState = stateMachineGraph.getNextState(currentState.getState(), handlerResponse.getEvent());
            currentState.setState(newState);
            stateMachineRepository.save(currentState);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}