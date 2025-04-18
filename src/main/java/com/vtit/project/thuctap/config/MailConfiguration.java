package com.vtit.project.thuctap.config;

import com.vtit.project.thuctap.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "notification.scheduler.enabled", havingValue = "true")
public class MailConfiguration {
    private final MailService mailService;


    @EventListener(ApplicationReadyEvent.class)
    public void sendBorrowNotificationsOnStartup() {
        mailService.sendNotifications();
    }

    @Scheduled(cron = "${notification.scheduler.cron}")
    public void sendBorrowNotifications() {
        mailService.sendNotifications();
    }
}
