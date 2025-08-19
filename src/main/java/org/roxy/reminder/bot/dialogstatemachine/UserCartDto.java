package org.roxy.reminder.bot.dialogstatemachine;

import lombok.Data;

@Data
public class UserCartDto {
    private Long chatId;
    private String city;
    private String street;
}
