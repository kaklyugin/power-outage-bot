package org.roxy.reminder.bot.service.notification;

import org.roxy.reminder.bot.persistence.repository.NotificationRepository;
import org.roxy.reminder.bot.service.http.BotClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationPublisherService {
    private final NotificationRepository notificationRepository;
    private final BotClient botClient;

    public NotificationPublisherService(NotificationRepository notificationRepository, BotClient botClient) {
        this.notificationRepository = notificationRepository;
        this.botClient = botClient;
    }

    //TODO Добавить асинхронную отправку
    @Scheduled(cron = "0/5 * * * * ?")
    private void publish() {
        notificationRepository.findByIsNotified(false).forEach(notification -> {

        });
    }
}
