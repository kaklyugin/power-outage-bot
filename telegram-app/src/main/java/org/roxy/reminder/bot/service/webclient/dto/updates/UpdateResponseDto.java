package org.roxy.reminder.bot.service.webclient.dto.updates;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.roxy.reminder.bot.jackson.UpdateResponseDtoDeserializer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = UpdateResponseDtoDeserializer.class)
public class UpdateResponseDto {
    private long updateId;
    private UpdateType updateType;
    private String callbackQueryId;
    private long fromId;
    private long chatId;
    private long date;
    private String userResponse;
    private long sourceMessageId;
}

