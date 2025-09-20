package org.roxy.reminder.bot.service.webclient;

import org.roxy.reminder.bot.service.webclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.service.webclient.dto.message.response.SendMessageResponseDto;

import java.util.List;

public interface BotClient {
    SendMessageResponseDto sendMessage(MessageDto message);
    List<SendMessageResponseDto> sendMessagesAsync(List<MessageDto> messages);
    String getUpdates();
    void answerCallbackQuery(String callbackQueryId);
}
