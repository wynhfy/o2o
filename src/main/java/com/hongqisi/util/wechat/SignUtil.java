package com.hongqisi.util.wechat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 微信请求校验工具
 */
public class SignUtil {

    //与接口配置信息中的token要一致
    private static String token="myo2o";

    /**
     * 验证签名
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkSignature(String signature,String timestamp,String nonce){
        String[] arr=new String[]{token,timestamp,nonce};
        //将token,timestamp,nonce三个参数进行排序
        Arrays.sort(arr);
        StringBuilder content=new StringBuilder();
        for(int i=0;i<arr.length;i++){
            content.append(arr[i]);
        }
        //获取加密对象
        MessageDigest md=null;
        String tmpStr=null;
        try{
            //获取SHA-1算法的实例
            md=MessageDigest.getInstance("SHA-1");
            //将三个参数拼接成一个字符串进行加密
            byte[] digest=md.digest(content.toString().getBytes());
            //将获取的byte数组转换为String
            tmpStr=byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        content=null;
        //将sha-1加密过后的字符串与signsture进行对比，标识该请求来源于微信
        return tmpStr!=null? tmpStr.equals(signature.toUpperCase()) :false;
    }

    /**
     * 将字节数组转换为16进制的字符串
     * @param bytes
     * @return
     */
    private static String byteToStr(byte[] bytes){
        String strDigest="";
        for(int i=0;i<bytes.length;i++){
            strDigest+= byteToHexStr(bytes[i]);
        }
        return strDigest;
    }

    /**
     * 将单个字节转换为16进制字符
     * @param mbyte
     * @return
     */
    private static String byteToHexStr(byte mbyte){
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] tempArr=new char[2];
        tempArr[0] = Digit[(mbyte >>> 4) & 0X0F];
        tempArr[1] = Digit[mbyte & 0X0F];
        String s = new String(tempArr);
        return s;
    }



}
