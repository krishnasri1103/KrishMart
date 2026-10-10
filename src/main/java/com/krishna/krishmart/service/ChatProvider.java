package com.krish.krishmart.service;

public interface ChatProvider {
    String getReply(String userMessage, String context);
}
