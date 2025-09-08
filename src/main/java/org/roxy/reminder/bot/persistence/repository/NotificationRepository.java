package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    void findByPowerOutageHashNotIn(List<Integer> collect);
    List<NotificationEntity> findByIsNotified(boolean isNotified);
}
