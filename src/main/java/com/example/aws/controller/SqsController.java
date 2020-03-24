package com.example.aws.controller;

import com.example.aws.service.SqsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

@RestController
@RequestMapping("/sqs")
@Slf4j
@RequiredArgsConstructor
public class SqsController {
    private final SqsService sqsService;

    @GetMapping("/send")
    public void sendMessage() {
            sqsService.sendMessage("Single message");
    }

    @GetMapping("/send100")
    public void sendHangedMessages() {
        IntStream.range(1, 100)
                .forEach(this::sendMessage);
    }

    private void sendMessage(String message) {
        sqsService.sendMessage(message);
    }

    private void sendMessage(int n) {
        var message = "hello from sqs controller " + n;
        sendMessage(message);
    }

}
