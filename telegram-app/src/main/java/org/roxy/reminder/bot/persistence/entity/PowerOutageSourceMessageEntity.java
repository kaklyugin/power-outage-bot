package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "power_outage_inbound_messages")
public class PowerOutageSourceMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city" ,columnDefinition = "TEXT")
    private String city;

    @Column(name = "address" ,columnDefinition = "TEXT")
    private String address;

    @Column
    private ZonedDateTime dateTimeOff;

    @Column
    private ZonedDateTime dateTimeOn;

    @Column(name = "power_outage_reason" ,columnDefinition = "TEXT")
    private String powerOutageReason;

    @Column (name = "url" ,columnDefinition = "TEXT")
    private String url;

    @Column(name = "normalized_street_type", columnDefinition = "TEXT")
    private String normalizedStreetType;

    @Column(name = "message_hash_code", unique = true)
    private Integer messageHashCode;

    @Version
    private LocalDateTime lastUpdatedAt;
}
