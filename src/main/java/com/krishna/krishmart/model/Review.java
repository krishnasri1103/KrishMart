package com.krishna.krishmart.model;

import java.sql.Timestamp;

/** Product review written by a buyer. */
public class Review {
    private long id; private long productId; private long userId; private String userName; private int rating; private String comment; private Timestamp createdAt;
    public long getId(){return id;} public void setId(long v){id=v;} public long getProductId(){return productId;} public void setProductId(long v){productId=v;}
    public long getUserId(){return userId;} public void setUserId(long v){userId=v;} public String getUserName(){return userName;} public void setUserName(String v){userName=v;}
    public int getRating(){return rating;} public void setRating(int v){rating=v;} public String getComment(){return comment;} public void setComment(String v){comment=v;}
    public Timestamp getCreatedAt(){return createdAt;} public void setCreatedAt(Timestamp v){createdAt=v;}
}