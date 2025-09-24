package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.StreetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StreetRepository extends JpaRepository<StreetEntity, String> {
    @Query(nativeQuery = true, value = "Select name from streets s where s.city_fias_id = :cityId and :streetName % name limit :limit")
    List<String> findWithFuzzySearchByCityIdAndName(@Param("cityId")String cityId,
                                                    @Param("streetName") String streetName,
                                                    @Param("limit") Integer limit);
}
