package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.roxy.reminder.bot.sate.machine.enums.State;
import org.roxy.reminder.bot.sate.machine.enums.StateMachineName;

import java.util.HashMap;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@Table(name = "state_machines")
public class StateMachineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;

    @Enumerated(EnumType.STRING)
    private State state;

    @Enumerated(EnumType.STRING)
    private StateMachineName stateMachineName;

    public StateMachineEntity(Long chatId, State state, StateMachineName stateMachineName) {
        this.chatId = chatId;
        this.state = state;
        this.stateMachineName = stateMachineName;
    }
}
