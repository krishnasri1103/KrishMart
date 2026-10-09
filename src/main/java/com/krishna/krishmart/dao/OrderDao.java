package com.krishna.krishmart.dao;

import com.krishna.krishmart.exception.AppException;
import com.krishna.krishmart.model.CartItem;
import com.krishna.krishmart.model.Order;
import java.math.BigDecimal;
import java.util.List;

/** Data access contract for orders and transactional checkout. */
public interface OrderDao {
    /** Atomically creates an order, decrements stock, and clears the buyer cart. */
    long createFromCart(long buyerId, List<CartItem> items, BigDecimal total) throws AppException;
    /** Lists one buyer's orders with item details. */
    List<Order> findByBuyer(long buyerId) throws AppException;
    /** Lists every order for administration. */
    List<Order> findAll() throws AppException;
    /** Lists orders containing products owned by a seller. */
    List<Order> findIncomingForSeller(long sellerId) throws AppException;
    /** Changes an order status. */
    void updateStatus(long orderId, String status) throws AppException;
    /** Checks whether a buyer has a delivered order containing a product. */
    boolean hasDeliveredProduct(long buyerId, long productId) throws AppException;
}