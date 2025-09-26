package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "location_types")
public class LocationTypeEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "type")
    private String type;

    @Column(name = "alias")
    private String alias;

    @Version
    private LocalDateTime lastUpdatedAt;
}
