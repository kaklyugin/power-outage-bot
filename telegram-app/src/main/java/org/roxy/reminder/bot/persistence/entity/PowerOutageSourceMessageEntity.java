package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
public class PowerOutageSourceMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String city;

    @Column
    private String address;

    @Column
    private ZonedDateTime dateTimeOff;

    @Column
    private ZonedDateTime dateTimeOn;

    @Column
    private String powerOutageReason;

    @Column(name = "hash_code", unique = true)
    private Integer hashCode;
}
