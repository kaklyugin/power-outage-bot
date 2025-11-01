package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.PowerOutageSourceMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PowerOutageSourceMessageRepository extends JpaRepository<PowerOutageSourceMessageEntity, Long> {

    @Query("select m.messageHashCode from PowerOutageSourceMessageEntity m where m.dateTimeOff > :dateTimeOff")
    List<Integer> findActualForDateTime(@Param("dateTimeOff") ZonedDateTime dateTimeOff );

    List<PowerOutageSourceMessageEntity> findAllByMessageHashCodeIn(List<Integer> messageHashCodes);

    Optional<PowerOutageSourceMessageEntity> findByMessageHashCode(Integer messageHashCode);

    List<PowerOutageSourceMessageEntity> findAllByMessageHashCodeInOrderByDateTimeOffAsc(Collection<Integer> messageHashCodes);

    @Query("SELECT p FROM PowerOutageSourceMessageEntity p WHERE p.isLocationFiasRequested = false AND p.isArchived = false ORDER BY p.id LIMIT :limit")
    List<PowerOutageSourceMessageEntity> findMessagesForEnrichment(@Param("limit") int limit);
}
