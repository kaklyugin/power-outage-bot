package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "location_types")
public class LocationTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "category", columnDefinition = "TEXT")
    private String category = "";

    @Column(name = "type", columnDefinition = "TEXT")
    private String type = "";

    @Column(name = "alias", columnDefinition = "TEXT")
    private String alias = "";

    @Version
    private LocalDateTime lastUpdatedAt;
}
