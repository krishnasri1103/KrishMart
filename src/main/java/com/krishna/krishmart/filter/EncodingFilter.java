package com.krishna.krishmart.filter;

import javax.servlet.*;
import java.io.IOException;

/** Applies UTF-8 encoding to every request and response. */
public class EncodingFilter implements Filter {
    /** Sets UTF-8 and continues the chain. */
    @Override public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain)throws IOException,ServletException{request.setCharacterEncoding("UTF-8");response.setCharacterEncoding("UTF-8");chain.doFilter(request,response);}
}