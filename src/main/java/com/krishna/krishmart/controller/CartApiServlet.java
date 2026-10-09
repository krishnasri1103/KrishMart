package com.krishna.krishmart.controller;

import com.krishna.krishmart.dto.ApiResponse;
import com.krishna.krishmart.dto.CartRequest;
import com.krishna.krishmart.util.JsonUtil;
import javax.servlet.http.*;
import java.io.IOException;

/** JSON endpoint used by the vanilla JavaScript cart controls. */
public class CartApiServlet extends BaseServlet {
    /** Applies a JSON cart mutation and returns the refreshed total. */
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws IOException{try{CartRequest x=JsonUtil.read(req,CartRequest.class);var service=cartService(req);long userId=currentUser(req).getId();if("add".equals(x.getAction()))service.add(userId,x.getProductId(),x.getQuantity());else if("update".equals(x.getAction()))service.update(userId,x.getProductId(),x.getQuantity());else service.remove(userId,x.getProductId());var items=service.get(userId);JsonUtil.write(resp,200,new ApiResponse<>(true,"Cart updated",service.total(items)));}catch(Exception e){JsonUtil.write(resp,400,new ApiResponse<>(false,e.getMessage(),null));}}
}