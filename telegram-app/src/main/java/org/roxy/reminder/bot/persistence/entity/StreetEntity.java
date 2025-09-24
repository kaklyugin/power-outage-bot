package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "streets")
@Data
public class StreetEntity {

    @Id
    @Column(name = "fias_id")
    private String fiasId;

    @Column(name = "city_fias_id")
    private String cityFiasId;

    @Column(name = "city_with_type")
    private String cityWithType;

    @Column(name = "city_type")
    private String cityType;

    @Column(name = "city_type_full")
    private String cityTypeFull;

    @Column(name = "city")
    private String city;

    @Column(name = "settlement_fias_id")
    private String settlementFiasId;

    @Column(name = "settlement_with_type")
    private String settlementWithType;

    @Column(name = "settlement_type")
    private String settlementType;

    @Column(name = "settlement_type_full")
    private String settlementTypeFull;

    @Column(name = "settlement")
    private String settlement;

    @Column(name = "street_fias_id")
    private String streetFiasId;

    @Column(name = "street_with_type")
    private String streetWithType;

    @Column(name = "street_type")
    private String streetType;

    @Column(name = "street_type_full")
    private String streetTypeFull;

    @Column(name = "street")
    private String street;

    @Version
    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;
}
