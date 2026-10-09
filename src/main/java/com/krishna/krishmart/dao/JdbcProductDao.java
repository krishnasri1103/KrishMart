package com.krishna.krishmart.dao;

import com.krishna.krishmart.exception.AppException;
import com.krishna.krishmart.model.Product;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** JDBC implementation of {@link ProductDao}. */
public class JdbcProductDao implements ProductDao {
    private final DataSource dataSource;
    /** Creates a DAO backed by the supplied pool. */
    public JdbcProductDao(DataSource dataSource){this.dataSource=dataSource;}
    /** {@inheritDoc} */
    @Override public List<Product> search(String keyword,String category)throws AppException{
        String sql="SELECT p.id,p.seller_id,u.name seller_name,p.name,p.description,p.price,p.stock_qty,p.category,p.image_url,p.created_at FROM products p JOIN users u ON u.id=p.seller_id WHERE (? IS NULL OR LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) AND (? IS NULL OR p.category=?) ORDER BY p.created_at DESC";
        String term=keyword==null||keyword.isBlank()?null:"%"+keyword.trim().toLowerCase()+"%";
        List<Product> out=new ArrayList<>();
        try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setString(1,term);p.setString(2,term);p.setString(3,term);p.setString(4,category);p.setString(5,category);try(ResultSet r=p.executeQuery()){while(r.next())out.add(map(r));}return out;}
        catch(SQLException e){throw new AppException("Unable to search products.",e);}
    }
    /** {@inheritDoc} */
    @Override public Optional<Product> findById(long id)throws AppException{
        String sql="SELECT p.id,p.seller_id,u.name seller_name,p.name,p.description,p.price,p.stock_qty,p.category,p.image_url,p.created_at FROM products p JOIN users u ON u.id=p.seller_id WHERE p.id=?";
        try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setLong(1,id);try(ResultSet r=p.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}catch(SQLException e){throw new AppException("Unable to find product.",e);}
    }
    /** {@inheritDoc} */
    @Override public List<Product> findBySeller(long sellerId)throws AppException{
        String sql="SELECT p.id,p.seller_id,u.name seller_name,p.name,p.description,p.price,p.stock_qty,p.category,p.image_url,p.created_at FROM products p JOIN users u ON u.id=p.seller_id WHERE p.seller_id=? ORDER BY p.created_at DESC";
        List<Product> out=new ArrayList<>();try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setLong(1,sellerId);try(ResultSet r=p.executeQuery()){while(r.next())out.add(map(r));}return out;}catch(SQLException e){throw new AppException("Unable to list seller products.",e);}
    }
    /** {@inheritDoc} */
    @Override public long create(Product x)throws AppException{
        String sql="INSERT INTO products(seller_id,name,description,price,stock_qty,category,image_url) VALUES(?,?,?,?,?,?,?)";
        try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){p.setLong(1,x.getSellerId());setFields(p,x,2);p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){if(r.next())return r.getLong(1);}}catch(SQLException e){throw new AppException("Unable to create product.",e);}throw new AppException("Product id was not generated.");
    }
    /** {@inheritDoc} */
    @Override public void update(Product x)throws AppException{
        String sql="UPDATE products SET name=?,description=?,price=?,stock_qty=?,category=?,image_url=? WHERE id=? AND seller_id=?";
        try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql)){setFields(p,x,1);p.setLong(7,x.getId());p.setLong(8,x.getSellerId());if(p.executeUpdate()!=1)throw new AppException("Product was not found or is not owned by this seller.");}catch(SQLException e){throw new AppException("Unable to update product.",e);}
    }
    /** {@inheritDoc} */
    @Override public void delete(long productId)throws AppException{try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM products WHERE id=?")){p.setLong(1,productId);p.executeUpdate();}catch(SQLException e){throw new AppException("Unable to remove product.",e);}}
    /** {@inheritDoc} */
    @Override public List<String> findCategories()throws AppException{List<String> out=new ArrayList<>();try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement("SELECT DISTINCT category FROM products ORDER BY category");ResultSet r=p.executeQuery()){while(r.next())out.add(r.getString(1));return out;}catch(SQLException e){throw new AppException("Unable to list categories.",e);}}
    private void setFields(PreparedStatement p,Product x,int start)throws SQLException{p.setString(start,x.getName());p.setString(start+1,x.getDescription());p.setBigDecimal(start+2,x.getPrice());p.setInt(start+3,x.getStockQty());p.setString(start+4,x.getCategory());p.setString(start+5,x.getImageUrl());}
    private Product map(ResultSet r)throws SQLException{return new Product(r.getLong("id"),r.getLong("seller_id"),r.getString("seller_name"),r.getString("name"),r.getString("description"),r.getBigDecimal("price"),r.getInt("stock_qty"),r.getString("category"),r.getString("image_url"),r.getTimestamp("created_at"));}
}