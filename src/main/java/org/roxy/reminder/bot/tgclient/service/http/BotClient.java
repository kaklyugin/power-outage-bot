package org.roxy.reminder.bot.tgclient.service.http;

import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.tgclient.dto.message.response.SendMessageResponseDto;

public interface BotClient {
    SendMessageResponseDto sendMessage(MessageDto message);

    String getUpdates();

    void answerCallbackQuery(String callbackQueryId);
}
