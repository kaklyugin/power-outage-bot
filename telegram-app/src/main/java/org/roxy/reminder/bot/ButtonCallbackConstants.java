package org.roxy.reminder.bot;

public enum ButtonCallbackConstants {
    OTHER_CITY ("OTHER_CITY"),
    BACK ("BACK");

    final String name;

    ButtonCallbackConstants(String name) {
        this.name = name;
    }
}
