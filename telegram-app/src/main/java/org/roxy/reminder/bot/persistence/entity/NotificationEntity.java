package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "power_outage_notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_cart_id", referencedColumnName = "id")
    private UserCartEntity userCart;

    @Column(name = "notification_text", columnDefinition = "TEXT")
    private String notificationText = "";

    @Column(name = "is_notified")
    private boolean isNotified;

    @Column(name = "notified_at")
    private ZonedDateTime notifiedAt;

    @Column(name = "message_hash_codes")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<Integer> messageHashCodes = new ArrayList<>();

    @Version
    private LocalDateTime lastUpdatedAt;
}
