package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<LocationEntity, String> {
    Optional<LocationEntity> findByLocationFiasId(String locationFiasId);
}
