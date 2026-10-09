package com.krishna.krishmart.dao;

import com.krishna.krishmart.exception.AppException;
import com.krishna.krishmart.model.Role;
import com.krishna.krishmart.model.User;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** JDBC implementation of {@link UserDao}. */
public class JdbcUserDao implements UserDao {
    private final DataSource dataSource;
    /** Creates a DAO backed by the supplied pool. */
    public JdbcUserDao(DataSource dataSource) { this.dataSource = dataSource; }
    /** {@inheritDoc} */
    @Override public Optional<User> findByEmail(String email) throws AppException {
        String sql = "SELECT id,name,email,password_hash,role,created_at FROM users WHERE LOWER(email)=LOWER(?)";
        try (Connection c=dataSource.getConnection(); PreparedStatement p=c.prepareStatement(sql)) {
            p.setString(1,email);
            try(ResultSet r=p.executeQuery()){ return r.next()?Optional.of(map(r)):Optional.empty(); }
        } catch(SQLException e){throw new AppException("Unable to read user.",e);}
    }
    /** {@inheritDoc} */
    @Override public long create(User user) throws AppException {
        String sql="INSERT INTO users(name,email,password_hash,role) VALUES(?,?,?,?)";
        try(Connection c=dataSource.getConnection(); PreparedStatement p=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            p.setString(1,user.getName());p.setString(2,user.getEmail());p.setString(3,user.getPasswordHash());p.setString(4,user.getRole().name());p.executeUpdate();
            try(ResultSet r=p.getGeneratedKeys()){if(r.next())return r.getLong(1);}
            throw new AppException("User id was not generated.");
        }catch(SQLException e){throw new AppException("Unable to create user.",e);}
    }
    /** {@inheritDoc} */
    @Override public List<User> findAll() throws AppException {
        List<User> result=new ArrayList<>(); String sql="SELECT id,name,email,password_hash,role,created_at FROM users ORDER BY created_at DESC";
        try(Connection c=dataSource.getConnection();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){while(r.next())result.add(map(r));return result;}
        catch(SQLException e){throw new AppException("Unable to list users.",e);}
    }
    private User map(ResultSet r)throws SQLException{return new User(r.getLong("id"),r.getString("name"),r.getString("email"),r.getString("password_hash"),Role.valueOf(r.getString("role")),r.getTimestamp("created_at"));}
}