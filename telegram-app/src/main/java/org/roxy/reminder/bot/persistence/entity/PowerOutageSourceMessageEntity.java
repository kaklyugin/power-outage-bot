package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "power_outage_inbound_messages")
public class PowerOutageSourceMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "location_fias_id")
    private String locationFiasId;

    @Column(name = "location_type")
    private String locationType;

    @Column(name = "street_type")
    private String streetType;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "buildings_numbers")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> buildingsNumbers;

    @Column(name = "date_time_off")
    private ZonedDateTime dateTimeOff;

    @Column(name = "date_time_on")
    private ZonedDateTime dateTimeOn;

    @Column(name = "power_outage_reason")
    private String powerOutageReason;

    @Column (name = "url")
    private String url;

    @Column(name = "message_hash_code")
    private Integer messageHashCode;

    @Column(name = "is_location_fias_requested")
    private boolean isLocationFiasRequested = false;

    @Column(name = "is_archived")
    private boolean isArchived = false;

    @Version
    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;
}