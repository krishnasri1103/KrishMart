package com.krish.krishmart.service;

import java.util.Properties;

public class ChatService {

    private final ChatProvider provider;
    
    private static final String CONTEXT = """
        You are KrishBot, a helpful assistant for KrishMart online store.
        KrishMart sells Fashion, Technology, Home & Living, Kitchen & Dining,
        Books & Media, Beauty & Care, Sports & Fitness, and Toys & Games.
        Answer ONLY questions about products, orders, shipping, and KrishMart.
        Keep answers short and friendly. Prices are in Indian Rupees.
        """;

    public ChatService(Properties props) {
        String providerType = props.getProperty("ai.chatbot.provider", "mock");
        if ("gemini".equals(providerType)) {
            this.provider = new GeminiChatProvider(props);
        } else {
            this.provider = new MockChatProvider();
        }
    }

    public String getReply(String message) {
        if (message == null || message.isBlank()) {
            return "Please type a message!";
        }
        if (message.length() > 500) {
            return "Message too long. Please keep it short!";
        }
        return provider.getReply(message, CONTEXT);
    }
}
