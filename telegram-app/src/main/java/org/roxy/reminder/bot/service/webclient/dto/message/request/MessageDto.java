package org.roxy.reminder.bot.service.webclient.dto.message.request;  //TODO переименовать

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.roxy.reminder.bot.service.webclient.dto.message.request.keyboard.InlineKeyboardDto;

@Data
@SuperBuilder(toBuilder = true)
public class MessageDto {

    @JsonProperty("chat_id")
    private String chatId;

    private String text;

    @JsonProperty("reply_markup")
    private InlineKeyboardDto replyMarkup;
}
