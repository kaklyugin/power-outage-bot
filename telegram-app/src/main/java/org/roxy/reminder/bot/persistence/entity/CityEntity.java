package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cities")
@Data
public class CityEntity {

    @Id
    @Column(name = "id")
    private String fiasId;

    private String name;
}
