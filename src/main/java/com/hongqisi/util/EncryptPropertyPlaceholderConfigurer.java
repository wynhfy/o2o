package com.hongqisi.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

   private String[] encryptPropNames={"jdbc.username","jdbc.password"};

    /**
     * 对关键属性进行转换
     * @param propertyName
     * @param propertyValue
     * @return
     */
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if(isEncryptProp(propertyName)){
            String decryptValue=DESUtil.getDecryptString(propertyValue);
            return decryptValue;
        }else{
            return propertyValue;
        }
    }


    /**
     * 判断该属性是否已经加密
     * @param propertyName
     * @return
     */
    private boolean isEncryptProp(String propertyName){
        //如果传入的字段与我们定义的字段名字相同则是加密的
        for(String encryptPropName:encryptPropNames){
            if(encryptPropName.equals(propertyName)){
                return true;
            }
        }
        return false;
    }


}
