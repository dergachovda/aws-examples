package com.example.sqs.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class SqsServiceImpl implements SqsService {

    private final QueueMessagingTemplate queueMessagingTemplate;
    private final AmazonSQSAsync sqsClient;
    private final MessageProcessorService messageProcessorService;

    @Value("${sqs.queue.name}") private String queue;

    @Value("${init.messages.quantity}") private Integer quantity;

    private AtomicInteger counter = new AtomicInteger(0);
    private Instant start;

    private void start(){
        start = Instant.now();
        log.info("Start polling");
    }

    private void stop(int n){
        var time = Duration.between(start, Instant.now()).toMillis();
        log.info("{} messages pulling was ended. It took {} ms", n, time);
    }


    @SqsListener(value = "${sqs.queue.name}", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    public void getMessage(String message) {
        if (counter.getAndIncrement() < 1) {
            start();
        }
        log.info("Message '{}' was received", message);
        messageProcessorService.execute(message);

        if (counter.get() >= quantity) {
            stop(quantity);
        }
    }
    
    @Override
    public void sendMessage(String message) {
        queueMessagingTemplate.send(queue, MessageBuilder.withPayload(message).build());
    }

    @PostConstruct
    public void init() {
        validateQueue();
//        addMessages();
    }

    private void addMessages() {
        log.info("Start adding messages");
        for(int i = 1; i <= quantity; i++) {
            var message = "msg_" + i;
            sendMessage(message);
        }
        log.info("{} messages were sent", quantity);
    }

    private void validateQueue() {
        try {
            GetQueueUrlResult queueUrlResult = sqsClient.getQueueUrl(queue);
            if (Objects.nonNull(queueUrlResult.getQueueUrl())) {
                var queueUrl = queueUrlResult.getQueueUrl();
                log.info("Queue {} already exists", queueUrl);
            }
        } catch (QueueDoesNotExistException e) {
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(queue);
            var queueUrl = sqsClient.createQueue(createQueueRequest).getQueueUrl();
            log.info("Queue {} created successfully", queueUrl);
        }
    }
}
