package com.example.aws.config;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;

@Profile("test")
@Configuration
public class SqsTestConfig {

    @Primary
    @Bean
    public AmazonSQSAsync amazonSQSAsync(LocalStackContainer localStackContainer) {
        return AmazonSQSAsyncClientBuilder
                .standard()
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.SQS))
                .build();
    }

}
