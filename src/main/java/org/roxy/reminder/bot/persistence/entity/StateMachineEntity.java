package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.roxy.reminder.bot.dialogstatemachine.enums.State;

import java.util.HashMap;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@Table(name = "state_machines")
public class StateMachineEntity {

    @Id
    private Long   chatId;

    @Enumerated(EnumType.STRING)
    private State state;

    private Long   lastBotMessageId;

    @Column(columnDefinition = "jsonb", name = "last_update_raw")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> lastUpdateRaw = new HashMap<>();

    public StateMachineEntity(Long chatId, State state) {
        this.chatId = chatId;
        this.state = state;
    }

    public void clear()
    {
        state = null ;
        lastUpdateRaw.clear();
        setLastBotMessageId(null);
    }
}
