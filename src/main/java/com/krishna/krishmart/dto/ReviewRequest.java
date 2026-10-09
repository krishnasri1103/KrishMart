package com.krishna.krishmart.dto;

/** JSON/form data used to submit a review. */
public class ReviewRequest {
    private long productId; private int rating; private String comment;
    public long getProductId(){return productId;} public void setProductId(long v){productId=v;} public int getRating(){return rating;} public void setRating(int v){rating=v;}
    public String getComment(){return comment;} public void setComment(String v){comment=v;}
}