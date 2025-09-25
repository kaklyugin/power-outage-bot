package org.roxy.reminder.bot.persistence.repository;

import org.roxy.reminder.bot.persistence.entity.UserAddressEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAddressesRepository extends JpaRepository<UserAddressEntity, Long> {
}