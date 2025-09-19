package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "streets")
@Data
public class StreetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String type;

    @ManyToOne
    @JoinColumn(name = "city_fias_id")
    private CityEntity city;

    @Column(name = "name" ,columnDefinition = "TEXT")
    private String name;

    @Version
    private LocalDateTime lastUpdatedAt;
}
