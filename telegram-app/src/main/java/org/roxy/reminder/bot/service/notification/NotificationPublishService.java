package org.roxy.reminder.bot.service.notification;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.roxy.reminder.bot.mapper.NotificationEntityToTgMessageMapper;
import org.roxy.reminder.bot.persistence.entity.NotificationEntity;
import org.roxy.reminder.bot.persistence.repository.NotificationRepository;
import org.roxy.reminder.bot.service.http.BotClient;
import org.roxy.reminder.bot.tgclient.dto.message.request.MessageDto;
import org.roxy.reminder.bot.tgclient.dto.message.response.SendMessageResponseDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationPublishService {
    private final NotificationRepository notificationRepository;
    private final BotClient botClient;
    private final NotificationEntityToTgMessageMapper mapper;

    public NotificationPublishService(NotificationRepository notificationRepository, BotClient botClient, NotificationEntityToTgMessageMapper mapper) {
        this.notificationRepository = notificationRepository;
        this.botClient = botClient;
        this.mapper = mapper;
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void publish() {
        log.info("Started scheduled notification publishing" );
        List<NotificationEntity> notificationEntityList = notificationRepository.findByNotifiedIsFalse();
        for(NotificationEntity notification: notificationEntityList) {
            MessageDto notificationMessage = mapper.mapNotificationEntityToMessageDto(notification);
            SendMessageResponseDto responseDto = botClient.sendMessage(notificationMessage);
            if (responseDto.isOk())
            {
                notification.setNotified(true);
            }
            notificationRepository.save(notification);
            //TODO Как корректно сохранять признак отправки ? Была ошибка
//            how to fix Caused by: org.hibernate.StaleObjectStateException:
//            Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect):
//            [org.roxy.reminder.bot.persistence.entity.NotificationEntity#599]
//            at org.hibernate.event.internal.DefaultMergeEventListener.entityIsDetached(DefaultMergeEventListener.java:426)
//            ~[hibernate-core-6.6.18.Final.jar:6.6.18.Final]
//            at org.hibernate.event.internal.DefaultMergeEventListener.merge(DefaultMergeEventListener.java:214)
//            ~[hibernate-core-6.6.18.Final.jar:6.6.18.Final] at org.hibernate.event.internal.DefaultMergeEventListener.doMerge(DefaultMergeEventListener.java:152) ~[hibernate-core-6.6.18.Final.jar:6.6.18.Final] at org.hibernate.event.internal.DefaultMergeEventListener.onMerge(DefaultMergeEventListener.java:136) ~[hibernate-core-6.6.18.Final.jar:6.6.18.Final] at org.hibernate.event.internal.DefaultMergeEventListener.onMerge(DefaultMergeEventListener.java:89) ~[hibernate-core-6.6.18.Final.jar:6.6.18.Final] at org.hibernate.event.service.internal.EventListenerGroupImpl.fireEventOnEachListener(EventListenerGroupImpl.java:127) ~[hibernate-core-6.6.18.Final.jar:6.6.18.Final] at org.hibernate.internal.SessionImpl.fireMerge(SessionImpl.java:854) ~[hibernate-core-6.6.18.Final.jar:6.6.18.Final] at org.hibernate.internal.SessionImpl.merge(SessionImpl.java:840) ~[hibernate-core-6.6.18.Final.jar:6.6.18.Final] at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104) ~[na:na] at java.base/java.lang.reflect.Method.invoke(Method.java:565)
//            ~[na:na] at org.springframework.orm.jpa.SharedEntityManagerCreator$SharedEntityManagerInvocationHandler.invoke(SharedEntityManagerCreator.java:320)
        }
        log.info("Sent {} notifications", notificationEntityList.size());
    }
}
