package com.krishna.krishmart.controller;

import com.krishna.krishmart.dao.*;
import com.krishna.krishmart.service.AdminService;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/** Renders administrator dashboards and listing moderation actions. */
public class AdminServlet extends BaseServlet {
    /** Shows users, orders, and products. */
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{try{AdminService admin=new AdminService(new JdbcUserDao(dataSource(req)),new JdbcProductDao(dataSource(req)),orderService(req));req.setAttribute("users",admin.users());req.setAttribute("orders",admin.orders());req.setAttribute("products",admin.products());req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req,resp);}catch(Exception e){fail(req,resp,e);}}
    /** Removes a listing or updates an order status. */
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{try{if("removeProduct".equals(req.getParameter("action"))){new JdbcProductDao(dataSource(req)).delete(Long.parseLong(req.getParameter("productId")));}else{orderService(req).updateStatus(Long.parseLong(req.getParameter("orderId")),req.getParameter("status"));}redirect(req,resp,"/admin/dashboard");}catch(Exception e){fail(req,resp,e);}}
}