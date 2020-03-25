package com.example.aws.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SnsServiceImpl implements SnsService {
    private final NotificationMessagingTemplate notificationMessagingTemplate;

    @Override
    public void send(String message) {
        send("subject", message);
        log.info("Sent message '{}'", message);
    }

    private void send(String subject, String message) {
        this.notificationMessagingTemplate.sendNotification("MyTopicName", message, subject);
    }

}
