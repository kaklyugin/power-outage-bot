package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.roxy.reminder.bot.dialogstatemachine.enums.State;

@Entity
@Data
@NoArgsConstructor
@Table(name = "state_machines")
public class StateMachineEntity {

    @Id
    private Long   chatId;

    @Enumerated(EnumType.STRING)
    private State state;

    public StateMachineEntity(Long chatId, State state) {
        this.chatId = chatId;
        this.state = state;
    }
}
