package com.krishna.krishmart.dao;

import com.krishna.krishmart.exception.AppException;
import com.krishna.krishmart.model.CartItem;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC implementation of {@link CartDao}. */
public class JdbcCartDao implements CartDao {
    private final DataSource dataSource;
    /** Creates a DAO backed by the supplied pool. */
    public JdbcCartDao(DataSource dataSource){this.dataSource=dataSource;}
    /** {@inheritDoc} */
    @Override public List<CartItem> findByUser(long userId)throws AppException{
        String sql="SELECT c.user_id,c.product_id,p.name,p.price,c.quantity,p.stock_qty FROM cart_items c JOIN products p ON p.id=c.product_id WHERE c.user_id=? ORDER BY c.created_at";
        List<CartItem> out=new ArrayList<>();try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setLong(1,userId);try(ResultSet r=p.executeQuery()){while(r.next())out.add(new CartItem(r.getLong(1),r.getLong(2),r.getString(3),r.getBigDecimal(4),r.getInt(5),r.getInt(6)));}return out;}catch(SQLException e){throw new AppException("Unable to load cart.",e);}
    }
    /** {@inheritDoc} */
    @Override public void add(long userId,long productId,int quantity)throws AppException{String sql="MERGE INTO cart_items(user_id,product_id,quantity) KEY(user_id,product_id) VALUES(?,?,COALESCE((SELECT quantity FROM cart_items WHERE user_id=? AND product_id=?),0)+?)";try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setLong(1,userId);p.setLong(2,productId);p.setLong(3,userId);p.setLong(4,productId);p.setInt(5,quantity);p.executeUpdate();}catch(SQLException e){throw new AppException("Unable to add item to cart.",e);}}
    /** {@inheritDoc} */
    @Override public void update(long userId,long productId,int quantity)throws AppException{try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement("UPDATE cart_items SET quantity=? WHERE user_id=? AND product_id=?")){p.setInt(1,quantity);p.setLong(2,userId);p.setLong(3,productId);p.executeUpdate();}catch(SQLException e){throw new AppException("Unable to update cart.",e);}}
    /** {@inheritDoc} */
    @Override public void remove(long userId,long productId)throws AppException{try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM cart_items WHERE user_id=? AND product_id=?")){p.setLong(1,userId);p.setLong(2,productId);p.executeUpdate();}catch(SQLException e){throw new AppException("Unable to remove cart item.",e);}}
    /** {@inheritDoc} */
    @Override public void clear(long userId)throws AppException{try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM cart_items WHERE user_id=?")){p.setLong(1,userId);p.executeUpdate();}catch(SQLException e){throw new AppException("Unable to clear cart.",e);}}
}