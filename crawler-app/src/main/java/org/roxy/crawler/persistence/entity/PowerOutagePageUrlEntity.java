package org.roxy.crawler.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "power_outage_source_pages")
public class PowerOutagePageUrlEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "page_sequence_generator")
    //@SequenceGenerator(name = "page_sequence_generator", sequenceName = "page_id_sequence_generator", initialValue = 1, allocationSize = 1)
    private Long id;
    private String pageUrl;
    @Version
    private LocalDateTime lastUpdatedAt;
}
