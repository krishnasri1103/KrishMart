package com.krishna.krishmart.service;

import com.krishna.krishmart.dao.ProductDao;
import com.krishna.krishmart.dao.UserDao;
import com.krishna.krishmart.exception.AppException;
import com.krishna.krishmart.model.User;
import java.util.List;

/** Coordinates administrator-only moderation views. */
public class AdminService {
    private final UserDao userDao; private final ProductDao productDao; private final OrderService orderService;
    /** Creates an administrator service. */
    public AdminService(UserDao userDao,ProductDao productDao,OrderService orderService){this.userDao=userDao;this.productDao=productDao;this.orderService=orderService;}
    /** Lists all users. */
    public List<User> users()throws AppException{return userDao.findAll();}
    /** Lists all orders. */
    public java.util.List<com.krishna.krishmart.model.Order> orders()throws AppException{return orderService.allOrders();}
    /** Lists all current listings. */
    public java.util.List<com.krishna.krishmart.model.Product> products()throws AppException{return productDao.search(null,null);}
    /** Removes a moderated listing. */
    public void removeProduct(long productId)throws AppException{productDao.delete(productId);}
}