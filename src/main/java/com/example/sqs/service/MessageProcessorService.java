package com.example.sqs.service;

public interface MessageProcessorService {
    void execute(String message);
}
