package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
@Table(name = "user_cart")
public class UserCartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_fias_id")
    private CityEntity city;

    @Column(name = "street")
    private String street;

    @Column(name = "normalized_street")
    private String normalizedStreet;

    @OneToMany(mappedBy = "userCart", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<NotificationEntity> notifications;

    @Version
    private LocalDateTime lastUpdatedAt;

    public UserCartEntity(Long chatId) {
        this.chatId = chatId;
    }

}
