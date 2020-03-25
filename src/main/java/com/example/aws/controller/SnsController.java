package com.example.aws.controller;

import com.example.aws.service.SnsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sns")
@Slf4j
@RequiredArgsConstructor
public class SnsController {
    private final SnsService snsService;

    @GetMapping("/send")
    public void sendMessage() {
        var message = "hello from sns controller";
        snsService.send(message);
    }

}
