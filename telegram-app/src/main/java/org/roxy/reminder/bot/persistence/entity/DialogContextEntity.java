package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
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

    @Column(name = "city")
    private String city;

    @Column(name = "street", columnDefinition = "TEXT")
    private String street;

    @Version
    private LocalDateTime lastUpdatedAt;

}
