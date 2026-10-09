package com.krishna.krishmart.controller;

import com.krishna.krishmart.dto.ProductRequest;
import com.krishna.krishmart.model.User;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/** Handles seller listing CRUD requests. */
public class SellerProductServlet extends BaseServlet {
    /** Shows the seller's inventory. */
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{try{req.setAttribute("products",productService(req).sellerProducts(currentUser(req).getId()));req.getRequestDispatcher("/WEB-INF/views/seller-products.jsp").forward(req,resp);}catch(Exception e){fail(req,resp,e);}}
    /** Creates, updates, or deletes a seller-owned listing. */
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        User user=currentUser(req);try{String action=req.getParameter("action");long id=parseId(req.getParameter("id"));if("delete".equals(action))productService(req).delete(user.getId(),id);else{ProductRequest p=new ProductRequest();p.setName(req.getParameter("name"));p.setDescription(req.getParameter("description"));p.setPrice(new BigDecimal(req.getParameter("price")));p.setStockQty(Integer.parseInt(req.getParameter("stockQty")));p.setCategory(req.getParameter("category"));p.setImageUrl(req.getParameter("imageUrl"));if("edit".equals(action))productService(req).update(user.getId(),id,p);else productService(req).create(user.getId(),p);}redirect(req,resp,"/seller/products");}catch(Exception e){fail(req,resp,e);}
    }
    private long parseId(String value){return value==null||value.isBlank()?0:Long.parseLong(value);}
}