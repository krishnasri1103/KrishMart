package com.krishna.krishmart.controller;

import com.krishna.krishmart.model.Role;
import com.krishna.krishmart.model.User;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/** Handles checkout and buyer/seller order history pages. */
public class OrderServlet extends BaseServlet {
    /** Renders buyer checkout/history or seller incoming orders based on the path. */
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{try{User u=currentUser(req);if(req.getServletPath().startsWith("/seller")){req.setAttribute("orders",orderService(req).sellerOrders(u.getId()));req.getRequestDispatcher("/WEB-INF/views/seller-orders.jsp").forward(req,resp);}else if("/app/checkout".equals(req.getServletPath())){var items=cartService(req).get(u.getId());req.setAttribute("items",items);req.setAttribute("total",cartService(req).total(items));req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req,resp);}else{req.setAttribute("orders",orderService(req).buyerOrders(u.getId()));req.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(req,resp);}}catch(Exception e){fail(req,resp,e);}}
    /** Places an order after the mock payment checkbox is confirmed. */
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{try{if(req.getServletPath().startsWith("/admin")){orderService(req).updateStatus(Long.parseLong(req.getParameter("orderId")),req.getParameter("status"));redirect(req,resp,"/admin/dashboard");return;}boolean confirmed="true".equals(req.getParameter("paymentConfirmed"));long id=orderService(req).checkout(currentUser(req).getId(),confirmed);req.getSession().setAttribute("flash","Order #"+id+" placed successfully.");redirect(req,resp,"/app/orders");}catch(Exception e){fail(req,resp,e);}}
}