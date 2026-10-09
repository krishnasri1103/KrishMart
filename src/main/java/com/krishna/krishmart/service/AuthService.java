package com.krishna.krishmart.service;

import com.krishna.krishmart.dao.UserDao;
import com.krishna.krishmart.exception.*;
import com.krishna.krishmart.model.Role;
import com.krishna.krishmart.model.User;
import com.krishna.krishmart.util.PasswordUtil;
import com.krishna.krishmart.util.ValidationUtil;

/** Applies registration and login rules without knowing JDBC details. */
public class AuthService {
    private final UserDao userDao;
    /** Creates an authentication service. */
    public AuthService(UserDao userDao){this.userDao=userDao;}
    /** Registers a buyer or seller; admin registration is intentionally unavailable. */
    public User register(String name,String email,String password,Role role)throws AppException{
        name=ValidationUtil.required(name,"Name",120);email=ValidationUtil.email(email);ValidationUtil.password(password);
        if(role!=Role.BUYER&&role!=Role.SELLER)throw new ValidationException("Choose Buyer or Seller.");
        if(userDao.findByEmail(email).isPresent())throw new ValidationException("An account with that email already exists.");
        User user=new User();user.setName(name);user.setEmail(email);user.setPasswordHash(PasswordUtil.hash(password));user.setRole(role);user.setId(userDao.create(user));return user;
    }
    /** Authenticates a user with bcrypt verification. */
    public User authenticate(String email,String password)throws AppException{
        email=ValidationUtil.email(email);ValidationUtil.password(password);
        User user=userDao.findByEmail(email).orElseThrow(()->new AuthenticationException("Email or password is incorrect."));
        if(!PasswordUtil.matches(password,user.getPasswordHash()))throw new AuthenticationException("Email or password is incorrect.");
        return user;
    }
}