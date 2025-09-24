package org.roxy.reminder.bot.service.formatter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LocationCategories {
    STREET ("street"),
    CITY("city");
    private final String value;
}
