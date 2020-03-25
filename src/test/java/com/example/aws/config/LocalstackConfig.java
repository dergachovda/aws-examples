package com.example.aws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;

@Profile("test")
@Configuration
public class LocalstackConfig {

    private static final LocalStackContainer.Service[] REQUIRED_SERVICES = {
            LocalStackContainer.Service.SQS,
            LocalStackContainer.Service.SNS,
    };

    private LocalStackContainer localStackContainer;

    @Bean
    public LocalStackContainer localStackContainer() {
        localStackContainer = new LocalStackContainer()
                .withServices(REQUIRED_SERVICES)
                .withEnv("HOSTNAME_EXTERNAL", "localhost")
                .withEnv("DOCKER_HOST", "unix:///var/run/docker.sock")
                .withEnv("USER", "localstack")
                .withPrivilegedMode(true);
        localStackContainer.start();
        return this.localStackContainer;
    }

}
