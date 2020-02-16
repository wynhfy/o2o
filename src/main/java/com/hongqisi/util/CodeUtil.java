package com.hongqisi.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {

    public static boolean checkVerifyCode(HttpServletRequest request){
        //得到实际生成的验证码
        String verifyCodeExcepted =(String)request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        //得到用户输入的验证码
        String verifyCodeActual =HttpServletRequestUtil.getString(request,"verifyCodeActual");
        if(verifyCodeActual ==null||!verifyCodeActual.equals(verifyCodeExcepted)){
            return false;
        }
        return true;
    }

}
