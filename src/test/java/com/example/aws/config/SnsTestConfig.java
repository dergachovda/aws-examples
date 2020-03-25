package com.example.aws.config;

import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;

@Profile("test")
@Configuration
public class SnsTestConfig {

    @Primary
    @Bean
    public AmazonSNSAsync amazonSNSAsync(LocalStackContainer localStackContainer) {
        return AmazonSNSAsyncClientBuilder
                .standard()
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.SNS))
                .build();
    }

}
