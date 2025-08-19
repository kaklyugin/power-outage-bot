package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    private Map<String, Object> lastUpdateRaw;

    public DialogContextEntity(Long chatId) {
        this.chatId = chatId;
    }
}
