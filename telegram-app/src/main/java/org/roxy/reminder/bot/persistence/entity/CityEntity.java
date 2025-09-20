package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "cities")
@Data
public class CityEntity {

    @Id
    @Column(name = "fias_id")
    private String fiasId;

    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    @Column(name = "type", columnDefinition = "TEXT")
    private String type;

    @Column(name = "district", columnDefinition = "TEXT")
    private String district;

    @Version
    private LocalDateTime lastUpdatedAt;
}
