package org.roxy.crawler.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Data @NoArgsConstructor @SuperBuilder
@Table(name = "power_outage_addresses")
public class PowerOutageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

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

    @Column (name = "url")
    private String url;

    @Column (name = "message_hash_code")
    private Integer messageHashCode;

    @Column (name = "comment")
    private String comment;

    @Column (name = "queue_sent_at")
    private ZonedDateTime queue_sent_at;

    @Version
    private LocalDateTime lastUpdatedAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
