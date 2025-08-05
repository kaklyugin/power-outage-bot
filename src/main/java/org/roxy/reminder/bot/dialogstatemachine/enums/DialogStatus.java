package org.roxy.reminder.bot.dialogstatemachine.enums;

public enum DialogStatus {
    NEW,
    WAITING_FOR_CITY_INPUT,
    WAITING_FOR_STREET_INPUT,
    DONE,
    STOPPED
}
