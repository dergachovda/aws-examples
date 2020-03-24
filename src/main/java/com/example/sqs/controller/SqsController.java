package com.example.sqs.controller;

import com.example.sqs.service.SqsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sqs")
@Slf4j
@RequiredArgsConstructor
public class SqsController {
    private final QueueMessagingTemplate queueMessagingTemplate;
    private final SqsService sqsService;

    @GetMapping
    public void sendMessage() {
        var n = 1000;
        for(int i = 0; i < n; i++) {
            var message = "hello from sqs controller " + i;
            sqsService.sendMessage(message);
        }
    }

}
