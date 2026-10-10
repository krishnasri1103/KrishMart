package com.krish.krishmart.service;

public class MockChatProvider implements ChatProvider {

    @Override
    public String getReply(String userMessage, String context) {
        String msg = userMessage.toLowerCase();

        if (msg.contains("price") || msg.contains("cost"))
            return "Our products range from ₹799 to ₹9,999. Check the shop for current prices!";
        if (msg.contains("shipping") || msg.contains("delivery"))
            return "We deliver within 3-5 business days across India.";
        if (msg.contains("return") || msg.contains("refund"))
            return "We offer 7-day easy returns on all products.";
        if (msg.contains("category") || msg.contains("product"))
            return "We have Fashion, Technology, Home & Living, Kitchen, Books, Beauty, Sports & Fitness!";
        if (msg.contains("payment"))
            return "We accept all major cards, UPI, and net banking.";
        if (msg.contains("contact") || msg.contains("help"))
            return "Email us at support@krishmart.local — we respond within 24 hours!";

        return "Hi! I'm KrishBot 🤖 Ask me about products, prices, shipping, or returns!";
    }
}
