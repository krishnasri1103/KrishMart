package com.kavi.kavimart.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.kavi.kavimart.dao.OrderDao;
import com.kavi.kavimart.dao.ProductDao;
import com.kavi.kavimart.model.Product;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class ChatServiceTest {
    private final ProductDao products = mock(ProductDao.class);
    private final OrderDao orders = mock(OrderDao.class);
    private final ChatService chat = new ChatService(products, orders);

    private static Product product(long id, String name, String price) {
        Product p = new Product(); p.setId(id); p.setName(name); p.setPrice(new BigDecimal(price)); p.setStockQty(5); return p;
    }

    @Test void emptyMessageAsksForInput() throws Exception {
        assertTrue(chat.reply("  ", null).text().contains("short question"));
    }

    @Test void orderQuestionAsksGuestToLogIn() throws Exception {
        assertTrue(chat.reply("where is my order", null).text().toLowerCase().contains("log in"));
    }

    @Test void priceLimitFiltersProducts() throws Exception {
        when(products.search(any(), any())).thenReturn(List.of(product(1, "Mug", "449.00"), product(2, "Bottle", "799.00")));
        ChatService.Reply r = chat.reply("home items under 500", null);
        assertEquals(1, r.products().size());
        assertEquals("Mug", r.products().get(0).getName());
    }
