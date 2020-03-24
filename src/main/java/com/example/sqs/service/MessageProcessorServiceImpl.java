package com.example.sqs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProcessorServiceImpl implements MessageProcessorService {

    @Qualifier("messageProcessorTaskExecutor")
    private final Executor executor;

    @Value("${processing-delay}") private int delay;

    @Override
    public void execute(String message) {
        executor.execute(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(delay);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            log.info("Message '{}' was processed", message);
        });
    }
}
