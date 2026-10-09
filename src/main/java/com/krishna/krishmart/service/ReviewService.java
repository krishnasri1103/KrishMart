package com.krishna.krishmart.service;

import com.krishna.krishmart.dao.ReviewDao;
import com.krishna.krishmart.exception.*;
import com.krishna.krishmart.model.Review;
import com.krishna.krishmart.util.ValidationUtil;
import java.util.List;

/** Enforces completed-order eligibility for product reviews. */
public class ReviewService {
    private final ReviewDao reviewDao; private final OrderService orderService;
    /** Creates a review service. */
    public ReviewService(ReviewDao reviewDao,OrderService orderService){this.reviewDao=reviewDao;this.orderService=orderService;}
    /** Lists product reviews. */
    public List<Review> forProduct(long productId)throws AppException{return reviewDao.findByProduct(productId);}
    /** Saves a review only when the buyer has a delivered purchase. */
    public void save(long buyerId,long productId,int rating,String comment)throws AppException{
        if(rating<1||rating>5)throw new ValidationException("Rating must be between 1 and 5.");
        if(!orderService.canReview(buyerId,productId))throw new ValidationException("Reviews are available after a delivered purchase.");
        Review review=new Review();review.setUserId(buyerId);review.setProductId(productId);review.setRating(rating);review.setComment(ValidationUtil.required(comment,"Comment",1000));reviewDao.save(review);
    }
}