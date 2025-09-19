package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.PowerOutageSourceMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface PowerOutageSourceMessageRepository extends JpaRepository<PowerOutageSourceMessageEntity, Long> {
    @Query("select m.messageHashCode from PowerOutageSourceMessageEntity m where m.dateTimeOff > :dateTimeOff")
    List<Integer> findActualForDateTime(@Param("dateTimeOff") ZonedDateTime dateTimeOff );

    List<PowerOutageSourceMessageEntity> findAllByMessageHashCodeIn(List<Integer> messageHashCodes);
}
