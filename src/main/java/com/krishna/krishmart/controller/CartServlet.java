package com.krishna.krishmart.controller;

import com.krishna.krishmart.model.User;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/** Renders the buyer cart and supports non-AJAX fallback mutations. */
public class CartServlet extends BaseServlet {
    /** Shows cart lines and the decimal running total. */
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{try{var items=cartService(req).get(currentUser(req).getId());req.setAttribute("items",items);req.setAttribute("total",cartService(req).total(items));req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req,resp);}catch(Exception e){fail(req,resp,e);}}
    /** Processes add, update, and remove form submissions. */
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{User u=currentUser(req);try{String action=req.getParameter("action");long productId=Long.parseLong(req.getParameter("productId"));if("add".equals(action))cartService(req).add(u.getId(),productId,Integer.parseInt(req.getParameter("quantity")));else if("update".equals(action))cartService(req).update(u.getId(),productId,Integer.parseInt(req.getParameter("quantity")));else cartService(req).remove(u.getId(),productId);redirect(req,resp,"/app/cart");}catch(Exception e){fail(req,resp,e);}}
}