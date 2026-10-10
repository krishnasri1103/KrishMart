package com.krish.krishmart.controller;

import com.krish.krishmart.service.ChatService;
import com.google.gson.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@WebServlet("/api/chat")
public class ChatServlet extends HttpServlet {

    private ChatService chatService;

    @Override
    public void init() throws ServletException {
        try {
            Properties props = new Properties();
            InputStream is = getServletContext()
                .getResourceAsStream("/WEB-INF/config.properties");
            if (is != null) props.load(is);
            chatService = new ChatService(props);
        } catch (Exception e) {
            throw new ServletException("ChatService init failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, 
                          HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        // Rate limiting — max 10 messages per session
        HttpSession session = req.getSession();
        Integer count = (Integer) session.getAttribute("chatCount");
        if (count == null) count = 0;
        if (count >= 10) {
            resp.getWriter().write("{\"reply\":\"Rate limit reached. Please wait.\"}");
            return;
        }
        session.setAttribute("chatCount", count + 1);

        // Read message
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }

        String message = "";
        try {
            JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
            message = json.get("message").getAsString();
        } catch (Exception e) {
            resp.getWriter().write("{\"reply\":\"Invalid request format.\"}");
            return;
        }

        String reply = chatService.getReply(message);

        JsonObject response = new JsonObject();
        response.addProperty("reply", reply);
        resp.getWriter().write(new Gson().toJson(response));
    }
}
