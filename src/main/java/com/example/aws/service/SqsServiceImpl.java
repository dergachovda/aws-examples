package com.example.aws.service;

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
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class SqsServiceImpl implements SqsService {

    private final QueueMessagingTemplate queueMessagingTemplate;
    private final AmazonSQSAsync sqsClient;
    private final MessageProcessorService messageProcessorService;

    @Value("${sqs.queue.name}") private String queue;

    @SqsListener(value = "${sqs.queue.name}", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    public void getMessage(String message) {
        log.info("Message '{}' was received", message);
        messageProcessorService.execute(message);
    }
    
    @Override
    public void sendMessage(String message) {
        queueMessagingTemplate.send(queue, MessageBuilder.withPayload(message).build());
    }

    @PostConstruct
    public void init() {
        validateQueue();
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
