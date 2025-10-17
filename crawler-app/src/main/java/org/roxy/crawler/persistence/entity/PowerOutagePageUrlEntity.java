package org.roxy.crawler.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "source_page_urls")
public class PowerOutagePageUrlEntity {
    @Id
    private Long id;
    private String pageUrl;
    private boolean enabled;
    @Version
    private LocalDateTime lastUpdatedAt;
}
