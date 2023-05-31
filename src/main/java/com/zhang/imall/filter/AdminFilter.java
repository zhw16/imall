package com.zhang.imall.filter;

import com.zhang.imall.common.Constant;
import com.zhang.imall.model.pojo.User;
import com.zhang.imall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 过滤器类javax.servlet
 * 功能：校验管理员信息
 */
public class AdminFilter implements Filter {
    //在service层获取用户信息
    @Autowired
    UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //先获取Session对象，以便从session对象中，获得当前登录用户。
        //先转为HttpServlet
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        //获取session对象,
        HttpSession session = httpServletRequest.getSession();
        //从session中获取当前登录用户
        User currentUser = (User) session.getAttribute(Constant.IMALL_USER);
        if (currentUser == null) {
            //用户没有登录
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n"
                    + "    \"status\": 10008,\n"
                    + "    \"msg\": \"USER_NEED_LOGIN\",\n"
                    + "    \"data\": null\n"
                    + "}");
            out.flush();
            out.close();
            return;
        }
        //校验是否为管理员
        boolean isRole = userService.checkAdminRole(currentUser);
        if (isRole) {//是管理员，继续执行
            filterChain.doFilter(servletRequest, servletResponse);
        } else {//不是管理员,提示需要管理员信息
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n"
                    + "    \"status\": 10008,\n"
                    + "    \"msg\": \"NEED_ADMIN_USER\",\n"
                    + "    \"data\": null\n"
                    + "}");
            out.flush();
            out.close();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
