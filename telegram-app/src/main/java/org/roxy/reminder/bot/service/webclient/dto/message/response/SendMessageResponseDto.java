package org.roxy.reminder.bot.service.webclient.dto.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageResponseDto {
    private boolean ok;
    private Long messageId;
    private Long chatId;

    //TODO переписать под обычный маппинг RESULT
    @JsonProperty("result")
    private void unpackMessageId(Map<String, Object> result) {
        this.messageId = Long.valueOf(result.get("message_id").toString());
        HashMap<String,Object> chat = (HashMap<String,Object>) result.get("chat");
        this.chatId = Long.valueOf(chat.get("id").toString());
    }
}
