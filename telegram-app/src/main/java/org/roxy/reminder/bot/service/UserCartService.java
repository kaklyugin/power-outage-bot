package org.roxy.reminder.bot.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.persistence.entity.CityEntity;
import org.roxy.reminder.bot.persistence.entity.LocationEntity;
import org.roxy.reminder.bot.persistence.entity.UserAddressEntity;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.CityRepository;
import org.roxy.reminder.bot.persistence.repository.LocationRepository;
import org.roxy.reminder.bot.persistence.repository.UserAddressesRepository;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserCartService {
    private final UserCartRepository userCartRepository;
    private final CityRepository cityRepository;
    private final LocationRepository locationRepository;
    private final UserAddressesRepository userAddressesRepository;

    public UserCartService(UserCartRepository userCartRepository, CityRepository cityRepository, LocationRepository locationRepository, UserAddressesRepository userAddressesRepository) {
        this.userCartRepository = userCartRepository;
        this.cityRepository = cityRepository;
        this.locationRepository = locationRepository;
        this.userAddressesRepository = userAddressesRepository;
    }

    @Transactional
    public void addCity(Long userCartId, String cityFiasId)
    {
        UserCartEntity userCart = userCartRepository.findById(userCartId).orElseThrow();
        UserAddressEntity userAddressEntity = new UserAddressEntity();
        CityEntity cityEntity = cityRepository.getReferenceById(cityFiasId);
        userAddressEntity.setCityEntity(cityEntity);
        userAddressEntity.setUserCart(userCart);
        userCart.getAddresses().add(userAddressEntity);
        userCartRepository.save(userCart);
    }

    @Transactional
    public void addLocation(Long userCartId, String streetFiasId)
    {
        UserCartEntity userCart = userCartRepository.findById(userCartId).orElseThrow();
        UserAddressEntity userAddressEntity = userCart.getAddresses().getLast();
        LocationEntity locationEntity = locationRepository.getReferenceById(streetFiasId);
        userAddressEntity.setLocationEntity(locationEntity);
        userAddressesRepository.save(userAddressEntity);
        userCartRepository.save(userCart);
    }

    public boolean checkLocationExists(String locationFiasId)
    {
         return locationRepository.findByLocationFiasId(locationFiasId).isPresent();
    }

    public UserCartEntity getUserCartByChatId(Long chatId)
    {
        return userCartRepository.findByChatId(chatId).orElseThrow(() -> new RuntimeException("User cart not found for chatId = " + chatId));
    }

    public UserCartEntity save(UserCartEntity userCartEntity)
    {
        return userCartRepository.save(userCartEntity);
    }
}
