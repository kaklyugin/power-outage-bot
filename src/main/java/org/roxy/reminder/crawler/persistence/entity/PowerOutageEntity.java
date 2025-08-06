package org.roxy.reminder.crawler.persistence.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@EntityListeners(AuditingEntityListener.class)
@Table(name = "power_outage_addresses")
public class PowerOutageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Column (name = "location")
    private String location;

    @Column (name = "address")
    private String address;

    @Column (name = "time_off")
    private ZonedDateTime dateTimeOff;

    @Column (name = "time_on")
    private ZonedDateTime dateTimeOn;

    @Column (name = "reason")
    private String powerOutageReason;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
