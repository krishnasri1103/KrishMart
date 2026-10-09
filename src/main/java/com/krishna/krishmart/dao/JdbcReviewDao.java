package com.krishna.krishmart.dao;

import com.krishna.krishmart.exception.AppException;
import com.krishna.krishmart.model.Review;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC implementation of {@link ReviewDao}. */
public class JdbcReviewDao implements ReviewDao {
    private final DataSource dataSource;
    /** Creates a DAO backed by the supplied pool. */
    public JdbcReviewDao(DataSource dataSource){this.dataSource=dataSource;}
    /** {@inheritDoc} */
    @Override public List<Review> findByProduct(long productId)throws AppException{List<Review> out=new ArrayList<>();String sql="SELECT r.id,r.product_id,r.user_id,u.name,r.rating,r.comment,r.created_at FROM reviews r JOIN users u ON u.id=r.user_id WHERE r.product_id=? ORDER BY r.created_at DESC";try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setLong(1,productId);try(ResultSet r=p.executeQuery()){while(r.next()){Review x=new Review();x.setId(r.getLong(1));x.setProductId(r.getLong(2));x.setUserId(r.getLong(3));x.setUserName(r.getString(4));x.setRating(r.getInt(5));x.setComment(r.getString(6));x.setCreatedAt(r.getTimestamp(7));out.add(x);}}return out;}catch(SQLException e){throw new AppException("Unable to list reviews.",e);}}
    /** {@inheritDoc} */
    @Override public void save(Review review)throws AppException{String sql="MERGE INTO reviews(product_id,user_id,rating,comment) KEY(product_id,user_id) VALUES(?,?,?,?)";try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setLong(1,review.getProductId());p.setLong(2,review.getUserId());p.setInt(3,review.getRating());p.setString(4,review.getComment());p.executeUpdate();}catch(SQLException e){throw new AppException("Unable to save review.",e);}}
}