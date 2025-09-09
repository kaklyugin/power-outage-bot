package org.roxy.reminder.bot.service.http;

import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.tgclient.dto.message.response.SendMessageResponseDto;

import java.util.List;

public interface BotClient {
    SendMessageResponseDto sendMessage(MessageDto message);
    List<SendMessageResponseDto> sendMessagesAsync(List<MessageDto> messages);
    String getUpdates();
    void answerCallbackQuery(String callbackQueryId);
}
