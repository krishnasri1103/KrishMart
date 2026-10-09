package com.krishna.krishmart.dto;

import java.math.BigDecimal;

/** JSON/form data used to create or update a product. */
public class ProductRequest {
    private String name; private String description; private BigDecimal price; private int stockQty; private String category; private String imageUrl;
    public String getName(){return name;} public void setName(String v){name=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;} public int getStockQty(){return stockQty;} public void setStockQty(int v){stockQty=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;} public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
}