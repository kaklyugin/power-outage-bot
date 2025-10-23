package org.roxy.crawler.persistence.repository;

import org.roxy.crawler.persistence.entity.PowerOutageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;

public interface PowerOutageRepository extends JpaRepository<PowerOutageEntity, Long> {
    @Query(nativeQuery = true, value = "update power_outage_addresses set queue_sent_at = now() where id = :id")
    void updateQueueSentAtTime(@Param("id") Integer id);
}
