package org.roxy.reminder.bot.tgclient.storage;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.roxy.reminder.bot.tgclient.dto.updates.UpdateResponseDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Component
public class ChatCaffeineStore implements ChatStore {
    private final Cache<Long, HashMap<Long, UpdateResponseDto>> cache = Caffeine.newBuilder()
            .expireAfterWrite(36, TimeUnit.HOURS)
            .build();

    @Override
    public void pushUpdate(Long chatId, Long updateId, UpdateResponseDto message) {
        HashMap<Long, UpdateResponseDto> updates = cache.get(chatId, list ->
                new HashMap<Long, UpdateResponseDto>() {{
                    put(updateId, message);
                }});
        updates.put(updateId, message);
        cache.put(chatId, updates);
    }

    @Override
    public boolean checkUpdateExists(Long chatId, Long updateId) {
        HashMap<Long, UpdateResponseDto> updates = cache.get(chatId, upd -> null);
        return updates != null && updates.containsKey(updateId);
    }
}
