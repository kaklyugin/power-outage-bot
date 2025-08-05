package org.roxy.reminder.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCartDto {
    private Long chatId;
    private String city;
    private String street;
}
