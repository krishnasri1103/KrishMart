package com.kavi.kavimart.service;

import com.kavi.kavimart.dao.OrderDao;
import com.kavi.kavimart.dao.ProductDao;
import com.kavi.kavimart.exception.AppException;
import com.kavi.kavimart.model.Order;
import com.kavi.kavimart.model.Product;
import com.kavi.kavimart.model.Role;
import com.kavi.kavimart.model.User;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** Rule-based shopping assistant: product search, price filters, order status and site help. */
public class ChatService {
    /** Chat answer with optional product suggestions. */
    public record Reply(String text, List<Product> products) {}

    private static final int MAX_LEN = 300, MAX_PRODUCTS = 4;
    private static final Pattern UNDER = Pattern.compile("(?:under|below|less than|within|upto|up to)\\s*(?:rs\\.?|₹)?\\s*(\\d{1,7})");
    private static final Set<String> STOP = Set.of("show","me","find","search","i","want","need","buy","a","an","the","some","for","please",
            "do","you","have","is","there","any","price","of","what","are","available","can","get","looking","to","in","with","and","cheap","best",
            "under","below","rs","upto","up","less","than","within","items","item","products","product");

    private final ProductDao productDao;
    private final OrderDao orderDao;

    /** Creates the assistant. */
    public ChatService(ProductDao productDao, OrderDao orderDao) { this.productDao = productDao; this.orderDao = orderDao; }

    /** Builds a reply for one message; {@code user} is null for guests. */
    public Reply reply(String raw, User user) throws AppException {
        String msg = raw == null ? "" : raw.trim();
        if (msg.isEmpty() || msg.length() > MAX_LEN) return text("Please type a short question (up to 300 characters).");
