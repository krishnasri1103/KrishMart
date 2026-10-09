package com.krishna.krishmart.filter;

import com.krishna.krishmart.model.Role;
import com.krishna.krishmart.model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/** Protects application, seller, and admin URL areas with session and role checks. */
public class AuthFilter implements Filter {
    /** Enforces authentication and role authorization. */
    @Override public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain)throws IOException,ServletException{
        HttpServletRequest req=(HttpServletRequest)request;HttpServletResponse resp=(HttpServletResponse)response;User user=(User)req.getSession().getAttribute("user");String path=req.getServletPath();
        if(user==null){resp.sendRedirect(req.getContextPath()+"/auth?action=login");return;}
        if(path.startsWith("/admin")&&user.getRole()!=Role.ADMIN){resp.sendError(HttpServletResponse.SC_FORBIDDEN);return;}
        if(path.startsWith("/seller")&&user.getRole()!=Role.SELLER){resp.sendError(HttpServletResponse.SC_FORBIDDEN);return;}
        if(path.startsWith("/app")&&user.getRole()!=Role.BUYER){resp.sendError(HttpServletResponse.SC_FORBIDDEN);return;}
        chain.doFilter(request,response);
    }
}