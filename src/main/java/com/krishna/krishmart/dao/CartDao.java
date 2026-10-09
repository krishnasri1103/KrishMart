package com.krishna.krishmart.dao;

import com.krishna.krishmart.exception.AppException;
import com.krishna.krishmart.model.CartItem;
import java.util.List;

/** Data access contract for buyer carts. */
public interface CartDao {
    /** Lists a user's cart lines. */
    List<CartItem> findByUser(long userId) throws AppException;
    /** Adds a quantity to an existing line or creates it. */
    void add(long userId, long productId, int quantity) throws AppException;
    /** Replaces a line quantity. */
    void update(long userId, long productId, int quantity) throws AppException;
    /** Removes one line. */
    void remove(long userId, long productId) throws AppException;
    /** Removes all lines after a successful checkout. */
    void clear(long userId) throws AppException;
}