package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.dto.CityDto;
import org.roxy.reminder.bot.persistence.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<CityEntity, String> {
    @Query("SELECT c FROM CityEntity c WHERE c.isDefaultTopCity = true ORDER BY c.name")
    List<CityEntity> findTopMenuDefaultCities();

    @Query("SELECT c FROM CityEntity c WHERE c.fiasId = :fiasId")
    Optional<CityEntity> findById(@Param("fiasId") String fiasId);

    @Query(value = """
            SELECT 
                fias_id as fiasId,
                name as name,
                district || ' ' || name as fullName
            FROM cities c
            WHERE :cityName % name 
            ORDER BY similarity(:cityName, name) DESC  """,
            nativeQuery = true
           )
    List<CityDto> findWithFuzzySearchByName(@Param("cityName") String cityName);
}