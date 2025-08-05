package org.roxy.reminder.bot.dialogstatemachine.storage;

import lombok.Getter;
import lombok.Setter;
import org.roxy.reminder.bot.dialogstatemachine.enums.DialogStatus;


public class ChatContext {

    @Getter @Setter
    private DialogStatus status = DialogStatus.NEW;
    @Getter
    private String city;
    @Getter @Setter
    private String street;
    @Getter @Setter
    private Long lastBotMessageId;

    public void setCity(String city) {
        this.city = city.replaceAll("CALLBACK_",city);
    }
}
