package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.catalina.User;

import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "power_outage_notifications")
public class PowerOutageNotificationEntity {

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

    @Column(name = "reason_message_hash_code")
    private Integer reasonMessageHashCode;
// TODO
// @Column(name = "last_notified_at")
// private ZonedDateTime lastNotifiedAt;
}
