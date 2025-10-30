package org.roxy.reminder.bot.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.persistence.entity.LocationEntity;
import org.roxy.reminder.bot.persistence.entity.UserLocationEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.CityRepository;
import org.roxy.reminder.bot.persistence.repository.LocationRepository;
import org.roxy.reminder.bot.persistence.repository.UserAddressesRepository;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserCartService {
    private final UserCartRepository userCartRepository;
    private final LocationRepository locationRepository;

    public UserCartService(UserCartRepository userCartRepository,
                           LocationRepository locationRepository
    ) {
        this.userCartRepository = userCartRepository;
        this.locationRepository = locationRepository;

    }

    @Transactional
    public void create(Long chatId, String locationFiasId) {
        UserCartEntity userCart = new UserCartEntity();
        userCart.setChatId(chatId);
        UserLocationEntity userLocationEntity = new UserLocationEntity();
        LocationEntity locationEntity = locationRepository.getReferenceById(locationFiasId);
        userLocationEntity.setLocationEntity(locationEntity);
        userLocationEntity.setUserCart(userCart);
        userCart.setLocations(List.of(userLocationEntity));
        userCartRepository.save(userCart);
    }

    @Transactional
    public void deleteByChatId(Long chatId) {
        userCartRepository.deleteById(chatId);
    }
}
