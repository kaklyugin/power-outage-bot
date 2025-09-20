package org.roxy.reminder.bot.cache;

import org.roxy.reminder.bot.service.webclient.dto.updates.UpdateResponseDto;

public interface ChatStore {
    void pushUpdate(Long chatId, Long updateId, UpdateResponseDto message);

    boolean checkUpdateExists(Long chatId, Long updateId);
}
