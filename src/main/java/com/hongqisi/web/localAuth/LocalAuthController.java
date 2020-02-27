package com.hongqisi.web.localAuth;


import com.hongqisi.dto.LocalAuthExecution;
import com.hongqisi.entity.LocalAuth;
import com.hongqisi.entity.PersonInfo;
import com.hongqisi.enums.LocalAuthStateEnum;
import com.hongqisi.exceptions.LocalAuthOperationException;
import com.hongqisi.service.LocalAuthService;
import com.hongqisi.util.CodeUtil;
import com.hongqisi.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/local",method = {RequestMethod.GET,RequestMethod.POST})
public class LocalAuthController {

    @Autowired
    private LocalAuthService localAuthService;

    @RequestMapping(value = "/bindlocalauth",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> bindLocalAuth(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码错误");
            return modelMap;
        }
        String username=HttpServletRequestUtil.getString(request,"userName");
        String password=HttpServletRequestUtil.getString(request,"password");
        PersonInfo user=(PersonInfo)request.getSession().getAttribute("user");
        if(username!=null&&password!=null&&user!=null&&user.getUserId()!=null){
            LocalAuth localAuth=new LocalAuth();
            localAuth.setUsername(username);
            localAuth.setPassword(password);
            localAuth.setPersonInfo(user);
            LocalAuthExecution lae=localAuthService.bindLocalAuth(localAuth);
            if(lae.getState()== LocalAuthStateEnum.SUCCESS.getState()){
                modelMap.put("success",true);
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg",lae.getStateInfo());
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","用户名和密码均不能为空");
        }
        return modelMap;
    }

    @RequestMapping(value = "/changelocalpwd",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> changeLocalPwd(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码输入错误");
        }
        String username=HttpServletRequestUtil.getString(request,"userName");
        String password=HttpServletRequestUtil.getString(request,"password");
        String newpassword=HttpServletRequestUtil.getString(request,"newPassword");
        PersonInfo user=(PersonInfo)request.getSession().getAttribute("user");
        if(username!=null&&password!=null&&newpassword!=null&&user!=null&&user.getUserId()!=null&&!password.equals(newpassword)){
              try{
                  LocalAuth localAuth=localAuthService.queryByUserId(user.getUserId());
                  if(localAuth==null||!username.equals(localAuth.getUsername())){
                      modelMap.put("success",false);
                      modelMap.put("errMsg","输入的账号非本次的账号");
                      return modelMap;
                  }
                  LocalAuthExecution lae=localAuthService.updateLocalAuth(user.getUserId(),username,password,newpassword);
                  if(lae.getState()==LocalAuthStateEnum.SUCCESS.getState()){
                      modelMap.put("success",true);
                  }else{
                      modelMap.put("success",false);
                      modelMap.put("errMsg",lae.getStateInfo());
                  }
              }catch (LocalAuthOperationException e){
                  modelMap.put("success",false);
                  modelMap.put("errMsg",e.getMessage());
              }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入密码");
        }
        return modelMap;
    }


    @ResponseBody
    @RequestMapping(value = "/logincheck",method = RequestMethod.POST)
    public Map<String,Object> loginCheck(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        boolean needVerify=HttpServletRequestUtil.getBoolean(request,"needVerify");
        if(needVerify&&!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
        }
        String username=HttpServletRequestUtil.getString(request,"userName");
        String password=HttpServletRequestUtil.getString(request,"password");
        if(username!=null&&password!=null){
            LocalAuth localAuth=localAuthService.queryByUserNameAndPwd(username,password);
            if(localAuth!=null){
                modelMap.put("success",true);
                request.getSession().setAttribute("user",localAuth.getPersonInfo());
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg","用户名或密码错误");
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入用户名和密码");
        }
        return modelMap;
    }

    @ResponseBody
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public Map<String,Object> logout(HttpServletRequest request){
       Map<String,Object> modelMap=new HashMap<>();
       request.getSession().setAttribute("user",null);
       modelMap.put("success",true);
       return  modelMap;
    }

}
