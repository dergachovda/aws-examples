package com.example.aws.service;

import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.example.aws.utils.SqsUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@Service
public class SnsServiceImpl implements SnsService {
    private final AmazonSNSAsync snsClient;
    private final AmazonSQSAsync sqsClient;
    private final NotificationMessagingTemplate notificationMessagingTemplate;

    @Value("${sns.topic.name}")
    private String topic;

    @Value("${sns.subscribe.queue.name}")
    private String subscribeQueue;

    @Getter
    private String topicArn;

    @PostConstruct
    public void init() {
        createTopic();
        addSubscribers();
        log.debug(snsClient.listTopics().toString());
    }

    @SqsListener(value = "${sns.subscribe.queue.name}", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    public void getMessage(String message) {
        log.info("Message '{}' was received from subscribeQueue", message);
    }

    private void createTopic() {
        CreateTopicResult createTopicResult = snsClient.createTopic(this.topic);
        topicArn = createTopicResult.getTopicArn();
    }

    private void addSubscribers() {
        var queueUrl = SqsUtils.createQueue(sqsClient, subscribeQueue);
        Topics.subscribeQueue(snsClient, sqsClient, topicArn, queueUrl);
    }

    @Override
    public void send(String message) {
        send("subject", message);
        log.info("Notification was sent - message: '{}'", message);
    }

    private void send(String subject, String message) {
        this.notificationMessagingTemplate.sendNotification(topic, message, subject);
    }

}
