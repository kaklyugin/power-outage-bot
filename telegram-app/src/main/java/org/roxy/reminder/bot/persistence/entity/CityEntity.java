package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "cities")
@Data
public class CityEntity {

    @Id
    @Column(name = "id")
    private String fiasId;

    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    @Version
    private LocalDateTime lastUpdatedAt;
}
