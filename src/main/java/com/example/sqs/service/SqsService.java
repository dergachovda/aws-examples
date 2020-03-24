package com.example.sqs.service;

import org.springframework.stereotype.Service;

public interface SqsService {
    void sendMessage(String message);
}
