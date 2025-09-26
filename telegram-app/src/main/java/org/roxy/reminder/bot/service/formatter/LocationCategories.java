package org.roxy.reminder.bot.service.formatter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LocationCategories {
    STREET ("street"),
    CITY("city");
    private final String value;
}
