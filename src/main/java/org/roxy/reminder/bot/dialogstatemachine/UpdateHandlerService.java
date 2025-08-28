package org.roxy.reminder.bot.dialogstatemachine;

import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.dialogstatemachine.handlers.UpdateHandler;
import org.roxy.reminder.bot.dto.UpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class UpdateHandlerService {

    private final List<UpdateHandler> updateHandlers;

    public UpdateHandlerService(List<UpdateHandler> updateHandlers)
    {
        this.updateHandlers = updateHandlers;
    }

    public void handle(UpdateDto update) {
        for(UpdateHandler handler: updateHandlers)
        {
            if (handler.canHandle(update))
            {
                handler.handle(update);
            }
        }
    }
}