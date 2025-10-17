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

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "district")
    private String district;

    @Column(name = "is_default_top_city")
    private boolean isDefaultTopCity;

    @Version
    private LocalDateTime lastUpdatedAt;
}
