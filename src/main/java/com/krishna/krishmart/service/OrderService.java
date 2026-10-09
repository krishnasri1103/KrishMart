package com.krishna.krishmart.service;

import com.krishna.krishmart.dao.CartDao;
import com.krishna.krishmart.dao.OrderDao;
import com.krishna.krishmart.exception.*;
import com.krishna.krishmart.model.CartItem;
import com.krishna.krishmart.model.Order;
import com.krishna.krishmart.model.OrderStatus;
import java.math.BigDecimal;
import java.util.List;

/** Coordinates mock-payment checkout and order views. */
public class OrderService {
    private final OrderDao orderDao; private final CartDao cartDao;
    /** Creates an order service. */
    public OrderService(OrderDao orderDao,CartDao cartDao){this.orderDao=orderDao;this.cartDao=cartDao;}
    /** Completes the mock payment confirmation and atomically creates an order. */
    public long checkout(long buyerId,boolean paymentConfirmed)throws AppException{
        if(!paymentConfirmed)throw new ValidationException("Mock payment must be confirmed before checkout.");
        List<CartItem> items=cartDao.findByUser(buyerId);if(items.isEmpty())throw new ValidationException("Your cart is empty.");
        for(CartItem item:items)if(item.getQuantity()>item.getStockQty())throw new InsufficientStockException("Not enough stock for "+item.getProductName()+".");
        BigDecimal total=items.stream().map(CartItem::getLineTotal).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2);
        return orderDao.createFromCart(buyerId,items,total);
    }
    /** Returns a buyer's order history. */
    public List<Order> buyerOrders(long buyerId)throws AppException{return orderDao.findByBuyer(buyerId);}
    /** Returns orders containing a seller's products. */
    public List<Order> sellerOrders(long sellerId)throws AppException{return orderDao.findIncomingForSeller(sellerId);}
    /** Returns every order for an administrator. */
    public List<Order> allOrders()throws AppException{return orderDao.findAll();}
    /** Updates an order status for admin workflow controls. */
    public void updateStatus(long orderId,String status)throws AppException{try{OrderStatus.valueOf(status);}catch(IllegalArgumentException e){throw new ValidationException("Invalid order status.");}orderDao.updateStatus(orderId,status);}
    /** Checks whether a buyer can review a product. */
    public boolean canReview(long buyerId,long productId)throws AppException{return orderDao.hasDeliveredProduct(buyerId,productId);}
}