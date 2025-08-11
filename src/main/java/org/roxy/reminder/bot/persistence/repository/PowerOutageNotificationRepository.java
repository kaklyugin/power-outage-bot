package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.PowerOutageNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerOutageNotificationRepository extends JpaRepository<PowerOutageNotificationEntity, Long> {
}
