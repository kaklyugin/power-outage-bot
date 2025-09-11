package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.CityEntity;
import org.roxy.reminder.bot.persistence.entity.StreetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends JpaRepository<CityEntity, String> {

}
