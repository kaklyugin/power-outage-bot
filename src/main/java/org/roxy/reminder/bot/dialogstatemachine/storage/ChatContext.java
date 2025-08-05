package org.roxy.reminder.bot.dialogstatemachine.storage;

import lombok.Getter;
import lombok.Setter;
import org.roxy.reminder.bot.dialogstatemachine.enums.DialogStatus;

@Getter
public class ChatContext {

    @Setter
    private DialogStatus status = DialogStatus.NEW;

    private String city;

    @Setter
    private String street;

    @Setter
    private Long lastBotMessageId;

    public void setCity(String city) {
        this.city = city.replaceAll("CALLBACK_", city);
    }
}
