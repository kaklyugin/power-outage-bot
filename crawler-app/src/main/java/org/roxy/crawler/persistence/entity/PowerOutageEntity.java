package org.roxy.crawler.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
@Table(name = "power_outage_addresses")
public class PowerOutageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Column (name = "city")
    private String city;

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

    @Column (name = "comment")
    private String comment;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
