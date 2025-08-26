package org.roxy.reminder.bot.dialogstatemachine.handlers.filloutcard.action;

import org.roxy.reminder.bot.dto.UpdateDto;
import org.roxy.reminder.bot.persistence.entity.DialogContextEntity;
import org.roxy.reminder.bot.persistence.entity.StateMachineEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;

public interface ActionHandler {
    ActionResponseDto handleAction(UpdateDto update, UserCartEntity userCartEntity);
}
