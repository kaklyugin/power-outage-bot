package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.StateMachineEntity;
import org.roxy.reminder.bot.persistence.entity.StreetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StreetRepository extends JpaRepository<StreetEntity, Long> {
    @Query(value = "Select name from streets where :streetName % name", nativeQuery = true)
    List<String> findFuzzyByName(@Param("streetName") String streetName);
}
