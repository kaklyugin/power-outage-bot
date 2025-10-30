package org.roxy.reminder.bot.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.persistence.dto.UserSessionDto;
import org.roxy.reminder.bot.persistence.entity.*;
import org.roxy.reminder.bot.persistence.repository.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserSessionCacheService {
    private final UserSessionCacheRepository userSessionCacheRepository;

    public UserSessionCacheService(UserSessionCacheRepository userSessionCacheRepository) {
        this.userSessionCacheRepository = userSessionCacheRepository;
    }

    @Transactional
    public UserSessionCacheEntity createSession(Long chatId) {
        return userSessionCacheRepository.save(UserSessionCacheEntity.builder().chatId(chatId).userSessionData(new UserSessionDto()).build());
    }

    @Transactional
    public UserSessionCacheEntity save(UserSessionCacheEntity entity) {
        return userSessionCacheRepository.save(entity);
    }

    @Transactional
    public void deleteSession(Long chatId) {
        userSessionCacheRepository.deleteById(chatId);
    }


    public UserSessionCacheEntity findByChatId(Long chatId) {
        return userSessionCacheRepository.findByChatId(chatId).orElseThrow();
    }

    @Transactional
    public void clearCity(Long chatId) {
        UserSessionCacheEntity userSessionCacheEntity = userSessionCacheRepository.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException("User session not found for chatId: " + chatId));
        userSessionCacheEntity.getUserSessionData().setCityFiasId(null);
        userSessionCacheRepository.save(userSessionCacheEntity);
    }

}
