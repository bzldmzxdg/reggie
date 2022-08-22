package com.dds.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.dds.reggie.config.BaseContext;
import com.dds.reggie.entity.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        1、获取本次请求的URI
        String requestURI = request.getRequestURI();
//        2、判断本次请求是否需要处理
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",//移动端发送短信
                "/user/login", //移动端登录
                "/user/loginout" //移动端注销
        };

        Boolean check = pathMatcher(urls, requestURI);
//        3、如果不需要处理，则直接放行
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }


//        4、判断管理端登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee")!= null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
            //向ThreadLocal内存放当前登录的用户id
            BaseContext.set((Long)request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            return;
        }
            //判断移动端登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user")!= null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));
            //向ThreadLocal内存放当前登录的用户id
            BaseContext.set((Long)request.getSession().getAttribute("user"));
            filterChain.doFilter(request,response);
            return;
        }
//        5、如果未登录则返回未登录结果
        if(request.getSession().getAttribute("employee")== null){
            log.info("用户未登录");
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return;
        }

    }


    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */

    public Boolean pathMatcher(String[] urls,String requestURI){

        for(String url : urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;

    }
}
