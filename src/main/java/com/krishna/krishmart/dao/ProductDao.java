package com.krishna.krishmart.dao;

import com.krishna.krishmart.exception.AppException;
import com.krishna.krishmart.model.Product;
import java.util.List;
import java.util.Optional;

/** Data access contract for product listings. */
public interface ProductDao {
    /** Searches active listings using optional keyword and category filters. */
    List<Product> search(String keyword, String category) throws AppException;
    /** Finds one listing by id. */
    Optional<Product> findById(long id) throws AppException;
    /** Lists a seller's listings. */
    List<Product> findBySeller(long sellerId) throws AppException;
    /** Creates a listing. */
    long create(Product product) throws AppException;
    /** Updates a seller-owned listing. */
    void update(Product product) throws AppException;
    /** Removes a listing. */
    void delete(long productId) throws AppException;
    /** Returns available category names. */
    List<String> findCategories() throws AppException;
}