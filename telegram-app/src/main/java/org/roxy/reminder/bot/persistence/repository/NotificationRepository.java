package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    void findByPowerOutageHashNotIn(List<Integer> collect);
    @Query ("select n from NotificationEntity n left join fetch n.userCart u where n.isNotified = false")
    List<NotificationEntity> findByNotifiedIsFalse();
}
