package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.roxy.reminder.bot.persistence.dto.UserSessionDto;

import java.time.LocalDateTime;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@Table(name = "user_sessions_cache")
public class UserSessionCacheEntity {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "session_data")
    @JdbcTypeCode(SqlTypes.JSON)
    private UserSessionDto userSessionData;

    @Version
    private LocalDateTime lastUpdatedAt;

}
