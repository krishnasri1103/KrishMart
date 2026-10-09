package com.krishna.krishmart.dao;

import com.krishna.krishmart.exception.AppException;
import com.krishna.krishmart.model.User;
import java.util.List;
import java.util.Optional;

/** Data access contract for users. */
public interface UserDao {
    /** Finds a user by its case-insensitive email. */
    Optional<User> findByEmail(String email) throws AppException;
    /** Inserts a buyer or seller and returns its generated id. */
    long create(User user) throws AppException;
    /** Lists all users for administration. */
    List<User> findAll() throws AppException;
}