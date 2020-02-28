package com.hongqisi.interceptor.shop;

import com.hongqisi.entity.PersonInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 店家管理系统登陆的验证拦截器
 */

public class ShopLoginInterceptor extends HandlerInterceptorAdapter {


    /**
     * 用户操作发生前，改写prehandle里的逻辑，进行拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //先从session中取出用户信息
        Object object=request.getSession().getAttribute("user");
        if(object!=null){
            PersonInfo personInfo=(PersonInfo)object;
            if(personInfo!=null&&personInfo.getUserId()!=null&&personInfo.getUserId()>0&&personInfo.getEnableStatus()==1){
                return true;
            }
        }
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("window.open ('" + request.getContextPath() + "/local/login?usertype=2','_self')");
        out.println("</script>");
        out.println("</html>");
        return false;

    }
}
