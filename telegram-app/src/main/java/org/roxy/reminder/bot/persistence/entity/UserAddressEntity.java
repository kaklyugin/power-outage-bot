package org.roxy.reminder.bot.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @JoinColumn(name = "location_fias_id")
    private LocationEntity locationEntity;

    @Version
    private LocalDateTime lastUpdatedAt;

}
