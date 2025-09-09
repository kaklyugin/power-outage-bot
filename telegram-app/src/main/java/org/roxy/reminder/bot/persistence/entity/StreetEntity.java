package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "streets")
@Data
public class StreetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String type;

    private String city;

    private String name;
}
