package com.krishna.krishmart.dto;

/** JSON request for one cart mutation. */
public class CartRequest {
    private String action; private long productId; private int quantity;
    public String getAction(){return action;} public void setAction(String v){action=v;} public long getProductId(){return productId;} public void setProductId(long v){productId=v;}
    public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
}