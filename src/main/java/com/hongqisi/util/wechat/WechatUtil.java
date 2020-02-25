package com.hongqisi.util.wechat;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongqisi.dto.wechat.UserAccessToken;
import com.hongqisi.dto.wechat.WechatUser;
import com.hongqisi.entity.PersonInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * 微信工具类
 */
public class WechatUtil {

    private static Logger logger= LoggerFactory.getLogger(WechatUtil.class);

    /**
     * 获取UserAccessToken实体类
     * @param code
     * @return
     * @throws IOException
     */
    public static UserAccessToken  getUserAccessToken (String code) throws IOException{
        //测试号信息里的appId
        String appId="wx43596eb36b4c3cfc";
        logger.debug("appId:"+appId);

        //测试号信息里的appsecret
        String appsecret="6eb093d44db76ce5e74e7fcd5883718e";
        logger.debug("appsecret:"+appsecret);

        //根据传入的code，拼接出访问微信定义好的接口的URL
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appsecret
                + "&code=" + code + "&grant_type=authorization_code";
        //向相应的URL发送请求获取token json字符串
        String tokenStr=httpsRequest(url,"GET",null);
        logger.debug("userAccessToken:"+tokenStr);

        UserAccessToken token=new UserAccessToken();
        ObjectMapper mapper=new ObjectMapper();
        try{
            token=mapper.readValue(tokenStr,UserAccessToken.class);
        }catch (Exception e){
            logger.error("获取用户accesstoken失败:"+e.toString());
            e.printStackTrace();
        }
        if(token==null){
            logger.error("获取用户accesstoken失败:null");
            return null;
        }
        return token;
    }

    /**
     * 获取WechatUser实体类
     * @param accessToken
     * @param openId
     * @return
     */
    public static WechatUser getWechatUser(String accessToken,String openId){
        //根据传入的accessToken以及openId拼接出访问微信定义的端口并获取用户信息的URL
        String url="https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId
                + "&lang=zh_CN";
        //访问该URL，获取用户信息json字符串
        String userStr=httpsRequest(url,"GET",null);
        logger.debug("user info:"+userStr);

        WechatUser wechatUser=new WechatUser();
        ObjectMapper mapper=new ObjectMapper();
        try{
            //将json字符串转换成对应的对象
            wechatUser=mapper.readValue(userStr,WechatUser.class);
        } catch (JsonParseException e) {
            logger.error("获取用户信息失败:"+e.toString());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            logger.error("获取用户信息失败:"+e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("获取用户信息失败:"+e.toString());
            e.printStackTrace();
        }
        if(wechatUser==null){
            logger.error("获取用户信息失败:null");
            return null;
        }
        return wechatUser;
    }

    /**
     * 将WechatUser里的信息转换为PersonInfo里的信息，并返回PersonInfo
     * @param wechatUser
     * @return
     */
     public static PersonInfo getPersonInfoFromRequest(WechatUser wechatUser){
         PersonInfo personInfo=new PersonInfo();
         personInfo.setName(wechatUser.getNickName());
         personInfo.setGender(wechatUser.getSex()+"");
         personInfo.setProfileImg(wechatUser.getHeadimgurl());
         personInfo.setEnableStatus(1);
         return personInfo;
     }


     /**
     * 发起https请求并获取结果
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式
     * @param outputStr     提交的数据
     * @return              json数据
     */
     public static String httpsRequest(String requestUrl,String requestMethod,String outputStr){
         StringBuffer buffer=new StringBuffer();
         try{
             //创建SSLContext对象，并使用我们指定的信任管理器初始化
             TrustManager[] tm={ new MyX509TrustManager() };
             SSLContext sslContext=SSLContext.getInstance("SSL","SunJSSE");
             sslContext.init(null,tm,new java.security.SecureRandom());
             //从上述的SSLContext对象中得到SSLSocketFactory对象
             SSLSocketFactory ssf=sslContext.getSocketFactory();

             URL url=new URL(requestUrl);
             HttpsURLConnection httpsUrlConnection=(HttpsURLConnection)url.openConnection();
             httpsUrlConnection.setSSLSocketFactory(ssf);
             httpsUrlConnection.setDoOutput(true);
             httpsUrlConnection.setDoInput(true);
             httpsUrlConnection.setUseCaches(false);
             //设置请求方式
             httpsUrlConnection.setRequestMethod(requestMethod);
             if("GET".equalsIgnoreCase(requestMethod)){
                 httpsUrlConnection.connect();
             }
             //当有数据需要提交时
             if(null!=outputStr){
                 OutputStream outputStream=httpsUrlConnection.getOutputStream();
                 //注意编码格式，防止中文乱码
                 outputStream.write(outputStr.getBytes("UTF-8"));
                 outputStream.close();
             }
             //将返回的输入流转换为字符串
             InputStream inputStream=httpsUrlConnection.getInputStream();
             InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"utf-8");
             BufferedReader bufferedReader=new BufferedReader(inputStreamReader);

             String str=null;
             while((str=bufferedReader.readLine())!=null){
                 buffer.append(str);
             }
             bufferedReader.close();
             inputStreamReader.close();

             //释放资源
             inputStream.close();
             inputStream=null;
             httpsUrlConnection.disconnect();

         } catch (NoSuchAlgorithmException e) {
             e.printStackTrace();
         } catch (NoSuchProviderException e) {
             e.printStackTrace();
         } catch (KeyManagementException e) {
             e.printStackTrace();
         } catch (MalformedURLException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return buffer.toString();
     }



}
