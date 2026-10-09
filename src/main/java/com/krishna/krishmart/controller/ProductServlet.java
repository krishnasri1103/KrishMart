package com.krishna.krishmart.controller;

import com.krishna.krishmart.model.Product;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/** Renders the public catalog and product details. */
public class ProductServlet extends BaseServlet {
    /** Serves catalog search or one product detail page. */
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        try{String id=req.getParameter("id");if(id!=null){Product p=productService(req).get(Long.parseLong(id));req.setAttribute("product",p);req.setAttribute("reviews",reviewService(req).forProduct(p.getId()));req.getRequestDispatcher("/WEB-INF/views/product.jsp").forward(req,resp);}else{req.setAttribute("products",productService(req).search(req.getParameter("q"),req.getParameter("category")));req.setAttribute("categories",productService(req).categories());req.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(req,resp);}}catch(Exception e){fail(req,resp,e);}
    }
}