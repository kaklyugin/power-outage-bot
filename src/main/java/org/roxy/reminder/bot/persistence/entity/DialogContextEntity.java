package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@Table(name = "dialogs_context")
public class DialogContextEntity {

    @Id
    private Long   chatId;
    private Long   lastBotMessageId;
    private String city;
    private String street;

    @Column(columnDefinition = "jsonb", name = "last_update_raw")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> lastUpdateRaw = new HashMap<>();

    public DialogContextEntity(Long chatId) {
        this.chatId = chatId;
    }

    public void clear()
    {
        lastUpdateRaw.clear();
        setLastBotMessageId(null);
        setCity(null);
        setStreet(null);
    }
}
