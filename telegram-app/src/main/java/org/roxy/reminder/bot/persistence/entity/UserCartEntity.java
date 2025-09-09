package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_cart")
public class UserCartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "normalized_street")
    private String normalizedStreet;

    @OneToMany(mappedBy = "userCart",cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
    private List<NotificationEntity> notifications;

    public UserCartEntity(Long chatId) {
        this.chatId = chatId;
    }

    // TODO
// @Column(name = "last_notified_at")
// private ZonedDateTime lastNotifiedAt;
}
