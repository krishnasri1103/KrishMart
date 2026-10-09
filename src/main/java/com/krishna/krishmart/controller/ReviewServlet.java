package com.krishna.krishmart.controller;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/** Receives buyer reviews for delivered products. */
public class ReviewServlet extends BaseServlet {
    /** Saves a review and returns to the product page. */
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{long productId=Long.parseLong(req.getParameter("productId"));try{reviewService(req).save(currentUser(req).getId(),productId,Integer.parseInt(req.getParameter("rating")),req.getParameter("comment"));redirect(req,resp,"/product?id="+productId);}catch(Exception e){fail(req,resp,e);}}
}