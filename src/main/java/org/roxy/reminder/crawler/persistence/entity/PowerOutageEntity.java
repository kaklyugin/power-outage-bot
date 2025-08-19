package org.roxy.reminder.crawler.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

//@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
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

    @Column (name = "hash_code")
    private Integer hashCode;
//TODO Check
//    @CreatedDate
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;

}
