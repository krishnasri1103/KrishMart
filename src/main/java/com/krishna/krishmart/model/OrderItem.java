package com.krishna.krishmart.model;

import java.math.BigDecimal;

/** A product line captured by an order. */
public class OrderItem {
    private long id; private long orderId; private long productId; private String productName; private int quantity; private BigDecimal unitPrice;
    public long getId(){return id;} public void setId(long v){id=v;} public long getOrderId(){return orderId;} public void setOrderId(long v){orderId=v;}
    public long getProductId(){return productId;} public void setProductId(long v){productId=v;} public String getProductName(){return productName;} public void setProductName(String v){productName=v;}
    public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;} public BigDecimal getUnitPrice(){return unitPrice;} public void setUnitPrice(BigDecimal v){unitPrice=v;}
}