package org.roxy.reminder.bot.tgclient.storage;

import org.roxy.reminder.bot.tgclient.dto.updates.UpdateResponseDto;

public interface ChatStore {
    void pushUpdate(Long chatId, Long updateId, UpdateResponseDto message);
    boolean checkUpdateExists(Long chatId, Long updateId);
}
