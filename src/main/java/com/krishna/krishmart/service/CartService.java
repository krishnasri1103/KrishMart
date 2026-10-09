package com.krishna.krishmart.service;

import com.krishna.krishmart.dao.CartDao;
import com.krishna.krishmart.dao.ProductDao;
import com.krishna.krishmart.exception.*;
import com.krishna.krishmart.model.CartItem;
import com.krishna.krishmart.model.Product;
import java.math.BigDecimal;
import java.util.List;

/** Applies inventory-aware cart rules. */
public class CartService {
    private final CartDao cartDao; private final ProductDao productDao;
    /** Creates a cart service. */
    public CartService(CartDao cartDao,ProductDao productDao){this.cartDao=cartDao;this.productDao=productDao;}
    /** Reads a buyer's cart. */
    public List<CartItem> get(long userId)throws AppException{return cartDao.findByUser(userId);}
    /** Adds a positive quantity without exceeding current stock. */
    public void add(long userId,long productId,int quantity)throws AppException{if(quantity<1)throw new ValidationException("Quantity must be at least 1.");Product p=productDao.findById(productId).orElseThrow(()->new NotFoundException("Product not found."));if(p.getStockQty()<quantity)throw new InsufficientStockException("Not enough stock available.");cartDao.add(userId,productId,quantity);}
    /** Replaces a cart quantity after validating stock. */
    public void update(long userId,long productId,int quantity)throws AppException{if(quantity<1){remove(userId,productId);return;}Product p=productDao.findById(productId).orElseThrow(()->new NotFoundException("Product not found."));if(p.getStockQty()<quantity)throw new InsufficientStockException("Not enough stock available.");cartDao.update(userId,productId,quantity);}
    /** Removes a cart line. */
    public void remove(long userId,long productId)throws AppException{cartDao.remove(userId,productId);}
    /** Calculates the cart total using decimal money values. */
    public BigDecimal total(List<CartItem> items){return items.stream().map(CartItem::getLineTotal).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2);}
}