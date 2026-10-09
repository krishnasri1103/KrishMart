package com.krishna.krishmart.controller;

import com.krishna.krishmart.exception.AppException;
import com.krishna.krishmart.model.Role;
import com.krishna.krishmart.model.User;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/** Handles buyer/seller registration, login, and logout. */
public class AuthServlet extends BaseServlet {
    /** Renders the requested auth form. */
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{String action=req.getParameter("action");req.setAttribute("authAction",action==null?"login":action);req.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(req,resp);}
    /** Processes credentials and redirects to the role landing page. */
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        String action=req.getParameter("action");
        try{
            if("logout".equals(action)){req.getSession().invalidate();redirect(req,resp,"/products");return;}
            if("register".equals(action)){Role role=Role.valueOf(req.getParameter("role"));User user=authService(req).register(req.getParameter("name"),req.getParameter("email"),req.getParameter("password"),role);login(req,user);redirect(req,resp,role==Role.SELLER?"/seller/products":"/products");return;}
            User user=authService(req).authenticate(req.getParameter("email"),req.getParameter("password"));login(req,user);
            if(user.getRole()==Role.ADMIN)redirect(req,resp,"/admin/dashboard");else if(user.getRole()==Role.SELLER)redirect(req,resp,"/seller/products");else redirect(req,resp,"/products");
        }catch(Exception e){req.setAttribute("authAction",action==null?"login":action);fail(req,resp,e);}
    }
    private void login(HttpServletRequest req,User user){HttpSession session=req.getSession();req.changeSessionId();session.setAttribute("user",user);session.setMaxInactiveInterval(30*60);}
}