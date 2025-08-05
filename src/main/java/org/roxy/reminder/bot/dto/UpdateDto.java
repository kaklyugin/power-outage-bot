package org.roxy.reminder.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.roxy.reminder.bot.tgclient.dto.updates.UpdateType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDto {
    private Long updateId;
    private UpdateType updateType;
    private String callbackQueryId;
    private Long fromId;
    private Long chatId;
    private Long date;
    private String userResponse;
    private Long sourceMessageId;
}
