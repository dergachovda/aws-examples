package com.example.aws.config;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.localstack.LocalStackContainer;

//@TestConfiguration
public class TestSqsConfig {

//    @Primary
//    @Bean
    public AmazonSQSAsync amazonSQSAsync(LocalStackContainer localStackContainer) {
        return AmazonSQSAsyncClientBuilder
                .standard()
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.SQS))
                .build();
    }

}
