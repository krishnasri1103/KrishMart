package com.krishna.krishmart.dao;

import com.krishna.krishmart.exception.AppException;
import com.krishna.krishmart.model.Review;
import java.util.List;

/** Data access contract for product reviews. */
public interface ReviewDao {
    /** Lists reviews for a product, newest first. */
    List<Review> findByProduct(long productId) throws AppException;
    /** Creates or replaces a buyer's review for a product. */
    void save(Review review) throws AppException;
}