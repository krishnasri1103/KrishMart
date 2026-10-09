package com.krishna.krishmart.controller;

import com.krishna.krishmart.dao.*;
import com.krishna.krishmart.exception.AppException;
import com.krishna.krishmart.model.User;
import com.krishna.krishmart.service.*;
import com.krishna.krishmart.listener.DataSourceContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;

/** Shared HTTP-only composition helpers for thin servlets. */
public abstract class BaseServlet extends HttpServlet {
    /** Gets the context-owned connection pool. */
    protected DataSource dataSource(HttpServletRequest request){return (DataSource)request.getServletContext().getAttribute(DataSourceContextListener.DATA_SOURCE_ATTRIBUTE);}
    /** Creates an authentication service for the current application context. */
    protected AuthService authService(HttpServletRequest r){return new AuthService(new JdbcUserDao(dataSource(r)));}
    /** Creates a product service for the current application context. */
    protected ProductService productService(HttpServletRequest r){return new ProductService(new JdbcProductDao(dataSource(r)));}
    /** Creates a cart service for the current application context. */
    protected CartService cartService(HttpServletRequest r){return new CartService(new JdbcCartDao(dataSource(r)),new JdbcProductDao(dataSource(r)));}
    /** Creates an order service for the current application context. */
    protected OrderService orderService(HttpServletRequest r){return new OrderService(new JdbcOrderDao(dataSource(r)),new JdbcCartDao(dataSource(r)));}
    /** Creates a review service for the current application context. */
    protected ReviewService reviewService(HttpServletRequest r){return new ReviewService(new JdbcReviewDao(dataSource(r)),orderService(r));}
    /** Returns the logged-in user. */
    protected User currentUser(HttpServletRequest r){return (User)r.getSession().getAttribute("user");}
    /** Forwards expected failures to the safe error page. */
    protected void fail(HttpServletRequest r,HttpServletResponse s,Exception e)throws ServletException,IOException{r.setAttribute("errorMessage",e instanceof AppException?e.getMessage():"Something went wrong. Please try again.");r.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(r,s);}
    /** Redirects without exposing the application context to JSP form code. */
    protected void redirect(HttpServletRequest r,HttpServletResponse s,String path)throws IOException{s.sendRedirect(r.getContextPath()+path);}
}