package com.krishna.krishmart.model;

import java.math.BigDecimal;

/** Cart line with its current product snapshot. */
public class CartItem {
    private long userId; private long productId; private String productName; private BigDecimal unitPrice; private int quantity; private int stockQty;
    public CartItem() {}
    public CartItem(long userId,long productId,String productName,BigDecimal unitPrice,int quantity,int stockQty){this.userId=userId;this.productId=productId;this.productName=productName;this.unitPrice=unitPrice;this.quantity=quantity;this.stockQty=stockQty;}
    public long getUserId(){return userId;} public void setUserId(long v){userId=v;} public long getProductId(){return productId;} public void setProductId(long v){productId=v;}
    public String getProductName(){return productName;} public void setProductName(String v){productName=v;} public BigDecimal getUnitPrice(){return unitPrice;} public void setUnitPrice(BigDecimal v){unitPrice=v;}
    public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;} public int getStockQty(){return stockQty;} public void setStockQty(int v){stockQty=v;}
    public BigDecimal getLineTotal(){return unitPrice.multiply(BigDecimal.valueOf(quantity));}
}