package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.dto.CityDto;
import org.roxy.reminder.bot.persistence.dto.LocationTypeDto;
import org.roxy.reminder.bot.persistence.entity.CityEntity;
import org.roxy.reminder.bot.persistence.entity.LocationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationTypeRepository extends JpaRepository<LocationTypeEntity, String> {
    @Query("Select l.id, l.type,l.alias from LocationTypeEntity l where l.category = :category")
    List<LocationTypeDto> getTypesByCategory(@Param("category") String category);
}