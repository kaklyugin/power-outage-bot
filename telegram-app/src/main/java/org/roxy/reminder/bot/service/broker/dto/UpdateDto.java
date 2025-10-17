package org.roxy.reminder.bot.service.broker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.roxy.reminder.bot.service.webclient.dto.updates.UpdateType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDto {
    private Long updateId;
    private UpdateType updateType;
    private String callbackQueryId;
    private Long fromId;
    private Long chatId;
    private String username;
    private Long date;
    private String userResponse;
    private Long sourceMessageId;
}
