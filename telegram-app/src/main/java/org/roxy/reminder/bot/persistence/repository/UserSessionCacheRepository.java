package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.entity.UserSessionCacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionCacheRepository extends JpaRepository<UserSessionCacheEntity, Long> {
    Optional<UserSessionCacheEntity> findByChatId(Long chatId);
}