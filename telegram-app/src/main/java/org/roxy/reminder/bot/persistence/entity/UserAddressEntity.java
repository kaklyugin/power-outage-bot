package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_addresses")
public class UserAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_cart_id")
    private UserCartEntity userCart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_fias_id")
    private CityEntity cityEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "street_fias_id")
    private StreetEntity streetEntity;

    @Version
    private LocalDateTime lastUpdatedAt;

}
