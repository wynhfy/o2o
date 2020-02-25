package com.hongqisi.web.wechat;

import com.hongqisi.dto.WechatAuthExecution;
import com.hongqisi.dto.wechat.UserAccessToken;
import com.hongqisi.dto.wechat.WechatUser;
import com.hongqisi.entity.PersonInfo;
import com.hongqisi.entity.WechatAuth;
import com.hongqisi.enums.WechatAuthStateEnum;
import com.hongqisi.service.PersonInfoService;
import com.hongqisi.service.WechatAuthService;
import com.hongqisi.util.wechat.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  获取关注公众号之后的微信用户信息的接口，如果在微信浏览器里访问
 *  https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx43596eb36b4c3cfc&redirect_uri=http://120.76.128.227/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 *  则这里将会获取到code,之后再可以通过code获取到access_token 进而获取到用户信息
 */

@Controller
@RequestMapping("wechatlogin")
public class WechatLoginController {

    private static Logger logger= LoggerFactory.getLogger(WechatLoginController.class);
    private static final String FRONTEND="1";
    private static final String SHOPEND="2";

    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private WechatAuthService wechatAuthService;


     @RequestMapping(value = "/logincheck",method = RequestMethod.GET)
     public String doGet(HttpServletRequest request, HttpServletResponse response){
        logger.debug("weixin login get...");
        // 获取微信公众号传输过来的code,通过code可获取access_token,进而获取用户信息
        String code=(String)request.getParameter("code");
        //这个state用来判断是返回首页界面还是店家管理系统
        String roleType=request.getParameter("state");
        WechatUser wechatUser=null;
        String openId=null;
        WechatAuth auth=null;

        if(null!=code){
            UserAccessToken token;
            try{
                //通过code获取access_token
                token= WechatUtil.getUserAccessToken(code);
                logger.debug("weixin login token:"+token.toString());

                //通过token获取accessToken
                String accessToken=token.getAccessToken();
                //通过token获取openId
                openId=token.getOpenId();
                //通过access_token和openId获取用户昵称等信息
                wechatUser=WechatUtil.getWechatUser(accessToken,openId);
                logger.debug("weixin login user:"+wechatUser.toString());
                request.getSession().setAttribute("openId",openId);

                //去后台查看是否已经有了这个微信账户了
                auth=wechatAuthService.getWechatAuthByOpenId(openId);

            } catch (IOException e) {
                logger.error("error in get Useraccesstoken or getwechatUser or findbyopenId"+e.toString());
                e.printStackTrace();
            }
        }

        // ======todo begin======
        // 前面咱们获取到openId后，可以通过它去数据库判断该微信帐号是否在我们网站里有对应的帐号了，
        // 没有的话这里可以自动创建上，直接实现微信与咱们网站的无缝对接。
        // ======todo end======


        //主要是为了将用户存入session中
        //如果没有这个微信账户，则就要创建一个
        if(auth==null){
            PersonInfo personInfo=new PersonInfo();
            personInfo=WechatUtil.getPersonInfoFromRequest(wechatUser);
            auth=new WechatAuth();
            auth.setOpenId(openId);
            if(FRONTEND.equals(roleType)){
                personInfo.setUserType(1);
            }else{
                personInfo.setUserType(2);
            }
            auth.setPersonInfo(personInfo);
            WechatAuthExecution we=wechatAuthService.register(auth);
            if(we.getState()!= WechatAuthStateEnum.SUCCESS.getState()){
                return null;
            }else{
                personInfo=personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
                request.getSession().setAttribute("user",personInfo);
            }
        }else{
            request.getSession().setAttribute("user",auth.getPersonInfo());
        }
        if(FRONTEND.equals(roleType)){
            return "frontend/index";
        }else{
            return "shop/shoplist";
        }

     }

}
