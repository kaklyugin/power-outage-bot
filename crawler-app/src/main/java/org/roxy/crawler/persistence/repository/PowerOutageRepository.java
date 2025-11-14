package org.roxy.crawler.persistence.repository;

import org.roxy.crawler.dto.ParsingStatus;
import org.roxy.crawler.persistence.entity.PowerOutageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PowerOutageRepository extends JpaRepository<PowerOutageEntity, Long> {

    @Modifying
    @Query("UPDATE PowerOutageEntity p SET p.queueSentAt = CURRENT_TIMESTAMP WHERE p.id = :id")
    void markAsSent(@Param("id") Long id);

    @Query("SELECT p FROM PowerOutageEntity p WHERE p.parsingStatus = 'SUCCESS'  and p.queueSentAt IS NULL")
    List<PowerOutageEntity> findNotSentMessages();
}
