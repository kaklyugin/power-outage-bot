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

    @OneToMany(mappedBy = "userCart", cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<NotificationEntity> notifications;

    @OneToMany(mappedBy = "userCart", cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.EAGER ,orphanRemoval = true)
    private List<UserLocationEntity> locations = new ArrayList<>();

    @Version
    private LocalDateTime lastUpdatedAt;

    public UserCartEntity(Long chatId) {
        this.chatId = chatId;
    }
}