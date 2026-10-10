package com.krish.krishmart.service;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.util.Properties;

public class GeminiChatProvider implements ChatProvider {

    private final String apiKey;
    private final String apiUrl;

    public GeminiChatProvider(Properties props) {
        this.apiKey = props.getProperty("gemini.api.key");
        this.apiUrl = props.getProperty("gemini.api.url");
    }

    @Override
    public String getReply(String userMessage, String context) {
        try {
            String prompt = context + "\nUser: " + userMessage;

            String body = """
                {
                  "contents": [{
                    "parts": [{"text": "%s"}]
                  }]
                }
                """.formatted(prompt.replace("\"", "'"));

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

            HttpResponse<String> response = 
                client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            return json.getAsJsonArray("candidates")
                       .get(0).getAsJsonObject()
                       .getAsJsonObject("content")
                       .getAsJsonArray("parts")
                       .get(0).getAsJsonObject()
                       .get("text").getAsString();

        } catch (Exception e) {
            return "Sorry, I'm having trouble right now. Please try again later.";
        }
    }
}
