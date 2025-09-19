package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserCartRepository extends JpaRepository<UserCartEntity, Long> {
    Optional<UserCartEntity> findByChatId(Long chatId);
}