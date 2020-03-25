package com.example.aws.utils;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class SqsUtils {
    public static String createQueue(AmazonSQS sqsClient, String queue) {
        String queueUrl = null;
        try {
            GetQueueUrlResult queueUrlResult = sqsClient.getQueueUrl(queue);
            if (Objects.nonNull(queueUrlResult.getQueueUrl())) {
                queueUrl = queueUrlResult.getQueueUrl();
                log.info("Queue {} already exists", queueUrl);
            }
        } catch (QueueDoesNotExistException e) {
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(queue);
            queueUrl = sqsClient.createQueue(createQueueRequest).getQueueUrl();
            log.info("Queue {} created successfully", queueUrl);
        }
        return queueUrl;
    }
}
