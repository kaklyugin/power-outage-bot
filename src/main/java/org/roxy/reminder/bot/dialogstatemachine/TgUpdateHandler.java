package org.roxy.reminder.bot.dialogstatemachine;

import org.roxy.reminder.bot.dialogstatemachine.handlers.CitySelectUpdateHandler;
import org.roxy.reminder.bot.dialogstatemachine.handlers.StartMessageHandler;
import org.roxy.reminder.bot.dialogstatemachine.handlers.StreetInputUpdateHandler;
import org.roxy.reminder.bot.dialogstatemachine.handlers.UpdateHandler;
import org.roxy.reminder.bot.dialogstatemachine.storage.ChatContext;
import org.roxy.reminder.bot.dialogstatemachine.storage.DialogContextStorage;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TgUpdateHandler {
    private final List<UpdateHandler> updateHandlers;
    private final DialogContextStorage dialogContextStorage;

    public TgUpdateHandler(List<UpdateHandler> updateHandlers, DialogContextStorage dialogContextStorage) {
        this.updateHandlers = updateHandlers;
        this.dialogContextStorage = dialogContextStorage;
        updateHandlers.add(new StartMessageHandler());
        updateHandlers.add(new CitySelectUpdateHandler());
        updateHandlers.add(new StreetInputUpdateHandler());
    }

    public void handle(UpdateDto update) {

        ChatContext context = dialogContextStorage.getOrCreateIfNotExistsContext(update.getChatId());

        for (UpdateHandler updateHandler : updateHandlers) {
            if (updateHandler.isApplicable(update, context)) {
                updateHandler.handleUpdate(update, context);
                break;
            }
        }
    }
}
