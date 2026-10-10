package com.kavi.kavimart.controller;

import com.kavi.kavimart.dao.JdbcOrderDao;
import com.kavi.kavimart.dao.JdbcProductDao;
import com.kavi.kavimart.dto.ApiResponse;
import com.kavi.kavimart.model.Product;
import com.kavi.kavimart.service.ChatService;
import com.kavi.kavimart.util.JsonUtil;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

/** Public JSON endpoint for the shopping assistant (POST /chat). */
public class ChatServlet extends BaseServlet {
    static class ChatRequest { String message; }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ChatRequest body = JsonUtil.read(req, ChatRequest.class);
            ChatService service = new ChatService(new JdbcProductDao(dataSource(req)), new JdbcOrderDao(dataSource(req)));
            ChatService.Reply reply = service.reply(body == null ? null : body.message, currentUser(req));
            List<Map<String, Object>> cards = new ArrayList<>();
            for (Product p : reply.products()) {
                Map<String, Object> c = new LinkedHashMap<>();
                c.put("name", p.getName()); c.put("price", p.getPrice()); c.put("stock", p.getStockQty());
                c.put("url", req.getContextPath() + "/product?id=" + p.getId());
                cards.add(c);
            }
            JsonUtil.write(resp, 200, new ApiResponse<>(true, reply.text(), cards));
        } catch (Exception e) {
            JsonUtil.write(resp, 500, new ApiResponse<>(false, "Sorry, something went wrong. Please try again.", null));
        }
    }
}
