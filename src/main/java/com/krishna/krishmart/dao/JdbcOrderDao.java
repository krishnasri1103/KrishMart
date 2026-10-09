package com.krishna.krishmart.dao;

import com.krishna.krishmart.exception.AppException;
import com.krishna.krishmart.exception.InsufficientStockException;
import com.krishna.krishmart.model.*;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC implementation of {@link OrderDao}, including the checkout transaction. */
public class JdbcOrderDao implements OrderDao {
    private final DataSource dataSource;
    /** Creates a DAO backed by the supplied pool. */
    public JdbcOrderDao(DataSource dataSource){this.dataSource=dataSource;}
    /** {@inheritDoc} */
    @Override public long createFromCart(long buyerId,List<CartItem> items,BigDecimal total)throws AppException{
        if(items.isEmpty())throw new AppException("Cart is empty.");
        String orderSql="INSERT INTO orders(buyer_id,status,total_amount) VALUES(?,'PENDING',?)";
        try(Connection c=dataSource.getConnection()){c.setAutoCommit(false);try(PreparedStatement order=c.prepareStatement(orderSql,Statement.RETURN_GENERATED_KEYS)){
            order.setLong(1,buyerId);order.setBigDecimal(2,total);order.executeUpdate();long orderId;try(ResultSet keys=order.getGeneratedKeys()){if(!keys.next())throw new AppException("Order id was not generated.");orderId=keys.getLong(1);}
            try(PreparedStatement stock=c.prepareStatement("UPDATE products SET stock_qty=stock_qty-? WHERE id=? AND stock_qty>=?");PreparedStatement line=c.prepareStatement("INSERT INTO order_items(order_id,product_id,quantity,unit_price) VALUES(?,?,?,?)");PreparedStatement clear=c.prepareStatement("DELETE FROM cart_items WHERE user_id=?")){
                for(CartItem item:items){stock.setInt(1,item.getQuantity());stock.setLong(2,item.getProductId());stock.setInt(3,item.getQuantity());if(stock.executeUpdate()!=1)throw new InsufficientStockException("Not enough stock for "+item.getProductName()+".");line.setLong(1,orderId);line.setLong(2,item.getProductId());line.setInt(3,item.getQuantity());line.setBigDecimal(4,item.getUnitPrice());line.addBatch();}
                line.executeBatch();clear.setLong(1,buyerId);clear.executeUpdate();c.commit();return orderId;
            }catch(Exception e){c.rollback();if(e instanceof AppException a)throw a;throw e;}
        }}catch(SQLException e){throw new AppException("Unable to place order.",e);}
    }
    /** {@inheritDoc} */
    @Override public List<Order> findByBuyer(long buyerId)throws AppException{return findOrders("WHERE o.buyer_id=?",buyerId);}
    /** {@inheritDoc} */
    @Override public List<Order> findAll()throws AppException{return findOrders("",null);}
    /** {@inheritDoc} */
    @Override public List<Order> findIncomingForSeller(long sellerId)throws AppException{
        String sql="SELECT DISTINCT o.id,o.buyer_id,u.name buyer_name,o.status,o.total_amount,o.created_at FROM orders o JOIN users u ON u.id=o.buyer_id JOIN order_items oi ON oi.order_id=o.id JOIN products p ON p.id=oi.product_id WHERE p.seller_id=? ORDER BY o.created_at DESC";
        List<Order> out=new ArrayList<>();try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setLong(1,sellerId);try(ResultSet r=p.executeQuery()){while(r.next())out.add(mapOrder(r));}loadItems(c,out);return out;}catch(SQLException e){throw new AppException("Unable to list incoming orders.",e);}
    }
    /** {@inheritDoc} */
    @Override public void updateStatus(long orderId,String status)throws AppException{try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement("UPDATE orders SET status=? WHERE id=?")){p.setString(1,status);p.setLong(2,orderId);if(p.executeUpdate()!=1)throw new AppException("Order not found.");}catch(SQLException e){throw new AppException("Unable to update order status.",e);}}
    /** {@inheritDoc} */
    @Override public boolean hasDeliveredProduct(long buyerId,long productId)throws AppException{String sql="SELECT 1 FROM orders o JOIN order_items i ON i.order_id=o.id WHERE o.buyer_id=? AND i.product_id=? AND o.status='DELIVERED'";try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setLong(1,buyerId);p.setLong(2,productId);try(ResultSet r=p.executeQuery()){return r.next();}}catch(SQLException e){throw new AppException("Unable to verify purchase.",e);}}
    private List<Order> findOrders(String ignored,Long value)throws AppException{String sql="SELECT o.id,o.buyer_id,u.name buyer_name,o.status,o.total_amount,o.created_at FROM orders o JOIN users u ON u.id=o.buyer_id WHERE (? IS NULL OR o.buyer_id=?) ORDER BY o.created_at DESC";List<Order> out=new ArrayList<>();try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql)){if(value==null){p.setNull(1,Types.BIGINT);p.setNull(2,Types.BIGINT);}else{p.setLong(1,value);p.setLong(2,value);}try(ResultSet r=p.executeQuery()){while(r.next())out.add(mapOrder(r));}loadItems(c,out);return out;}catch(SQLException e){throw new AppException("Unable to list orders.",e);}}
    private void loadItems(Connection c,List<Order> orders)throws SQLException{String sql="SELECT i.id,i.order_id,i.product_id,p.name,i.quantity,i.unit_price FROM order_items i JOIN products p ON p.id=i.product_id WHERE i.order_id=?";try(PreparedStatement p=c.prepareStatement(sql)){for(Order o:orders){p.setLong(1,o.getId());try(ResultSet r=p.executeQuery()){while(r.next()){OrderItem i=new OrderItem();i.setId(r.getLong(1));i.setOrderId(r.getLong(2));i.setProductId(r.getLong(3));i.setProductName(r.getString(4));i.setQuantity(r.getInt(5));i.setUnitPrice(r.getBigDecimal(6));o.getItems().add(i);}}}}}
    private Order mapOrder(ResultSet r)throws SQLException{Order o=new Order();o.setId(r.getLong(1));o.setBuyerId(r.getLong(2));o.setBuyerName(r.getString(3));o.setStatus(OrderStatus.valueOf(r.getString(4)));o.setTotalAmount(r.getBigDecimal(5));o.setCreatedAt(r.getTimestamp(6));return o;}
}