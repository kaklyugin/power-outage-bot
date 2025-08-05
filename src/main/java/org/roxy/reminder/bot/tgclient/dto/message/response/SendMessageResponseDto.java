package org.roxy.reminder.bot.tgclient.dto.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageResponseDto {
    private boolean ok;
    private Long messageId;

    @JsonProperty("result")
    private void unpackMessageId(Map<String, Object> result) {
        this.messageId = Long.valueOf(result.get("message_id").toString());
    }
}
