package org.roxy.reminder.bot.dialogstatemachine.storage;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class DialogContextStorage {
    private final ConcurrentHashMap<Long, ChatContext> dialogStateMachineMap = new ConcurrentHashMap<>();

    public ChatContext getOrCreateIfNotExistsContext(Long chatId) {
        if (dialogStateMachineMap.get(chatId) != null) {
            return dialogStateMachineMap.get(chatId);
        } else {
            dialogStateMachineMap.putIfAbsent(chatId, new ChatContext());
            return dialogStateMachineMap.get(chatId);
        }
    }
}
