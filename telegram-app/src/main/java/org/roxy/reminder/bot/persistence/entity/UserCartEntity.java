package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
@Table(name = "user_carts")
public class UserCartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "username")
    private String username;

    @OneToMany(mappedBy = "userCart", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<NotificationEntity> notifications;

    @OneToMany(mappedBy = "userCart", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER ,orphanRemoval = true)
    private List<UserAddressEntity> addresses = new ArrayList<>();

    @Version
    private LocalDateTime lastUpdatedAt;

    public UserCartEntity(Long chatId) {
        this.chatId = chatId;
    }
}