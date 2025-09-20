package org.roxy.reminder.bot.sate.machine;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.service.broker.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.StateMachineEntity;
import org.roxy.reminder.bot.persistence.repository.StateMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StateMachineProvider {
    @Autowired
    private ApplicationContext applicationContext;
    private final StateMachineRepository stateMachineRepository;

    public StateMachineProvider(StateMachineRepository stateMachineRepository) {
        this.stateMachineRepository = stateMachineRepository;
    }

    public StateMachine getStateMachine(UpdateDto update) {
        StateMachineEntity stateMachineEntity = stateMachineRepository.getByChatId(update.getChatId());
        try {
            return applicationContext.getBean(stateMachineEntity.getStateMachineName().getStateMachineClass());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
