package org.roxy.reminder.bot.service;

import jakarta.transaction.Transactional;
import org.roxy.reminder.bot.persistence.entity.UserCartEntity;
import org.roxy.reminder.bot.persistence.repository.UserCartRepository;
import org.springframework.stereotype.Service;

@Service
public class UserCartService {
    private final UserCartRepository userCartRepository;

    public UserCartService(UserCartRepository userCartRepository) {
        this.userCartRepository = userCartRepository;
    }

    @Transactional
    public UserCartEntity saveUserCart(Long chatId, String city, String street) {

        UserCartEntity userCartEntityToSave = new UserCartEntity();
        userCartEntityToSave.setChatId(chatId);
        userCartEntityToSave.setCity(city);
        userCartEntityToSave.setStreet(street);

        return userCartRepository.save(userCartEntityToSave);

    }
}
