package org.roxy.reminder.bot.events;

import lombok.Getter;
import lombok.ToString;
import org.roxy.reminder.bot.persistence.entity.PowerOutageSourceMessageEntity;
import org.springframework.context.ApplicationEvent;

@Getter @ToString
public class PowerOutageMessageEvent extends ApplicationEvent {
    private final PowerOutageSourceMessageEntity content;

    public PowerOutageMessageEvent(Object source, PowerOutageSourceMessageEntity content) {
        super(source);
        this.content = content;
    }
}