package com.krishna.krishmart.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** Customer order and its item lines. */
public class Order {
    private long id; private long buyerId; private String buyerName; private OrderStatus status; private BigDecimal totalAmount; private Timestamp createdAt;
    private List<OrderItem> items = new ArrayList<>();
    public long getId(){return id;} public void setId(long v){id=v;} public long getBuyerId(){return buyerId;} public void setBuyerId(long v){buyerId=v;}
    public String getBuyerName(){return buyerName;} public void setBuyerName(String v){buyerName=v;} public OrderStatus getStatus(){return status;} public void setStatus(OrderStatus v){status=v;}
    public BigDecimal getTotalAmount(){return totalAmount;} public void setTotalAmount(BigDecimal v){totalAmount=v;} public Timestamp getCreatedAt(){return createdAt;} public void setCreatedAt(Timestamp v){createdAt=v;}
    public List<OrderItem> getItems(){return items;} public void setItems(List<OrderItem> v){items=v;}
}