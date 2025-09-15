package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

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

    @Column(name = "notification_text")
    private String notificationText;

    @Column(name = "is_notified")
    private boolean isNotified;

    @Column(name = "notified_at")
    private ZonedDateTime notifiedAt;

    @Column(name = "message_hash_code")
    private Integer messageHashCode;
// TODO
// @Column(name = "last_notified_at")
// private ZonedDateTime lastNotifiedAt;
}
