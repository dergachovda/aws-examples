package com.example.sqs.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SqsConfig {

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    @Value("${sqs.queue.name}")
    private String queue;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.sqs-endpoint}")
    private String sqsEndpoint;


    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync() {
        ClientConfiguration clientConfig = new ClientConfiguration()
                .withMaxConnections(50);
        var sqsClient = AmazonSQSAsyncClientBuilder
                .standard()
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(accessKey, secretKey)))
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(sqsEndpoint, awsRegion))
                .withClientConfiguration(clientConfig)
                .build();

        return sqsClient;
    }

//    @Bean
//    public AsyncTaskExecutor asyncTaskExecutor() {
////        var taskExecutor = new SimpleAsyncTaskExecutor("my_te_");
////        taskExecutor.setConcurrencyLimit(20);
//        var taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setThreadNamePrefix("te_");
//        taskExecutor.setCorePoolSize(25);
//        taskExecutor.setMaxPoolSize(50);
//        taskExecutor.initialize();
//
//        return taskExecutor;
//    }
//
//    @Bean
//    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSqs/*, AsyncTaskExecutor asyncTaskExecutor*/) {
//        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
//        factory.setAmazonSqs(amazonSqs);
//        factory.setAutoStartup(true);
//        factory.setMaxNumberOfMessages(10);
////        factory.setTaskExecutor(asyncTaskExecutor);
//        return factory;
//    }

}
