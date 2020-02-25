package com.hongqisi.service;

import com.hongqisi.dto.WechatAuthExecution;
import com.hongqisi.entity.WechatAuth;
import com.hongqisi.exceptions.WechatAuthOperationException;

public interface WechatAuthService {

    /**
     * 根据openId获取账号
     * @param openId
     * @return
     */
    public WechatAuth getWechatAuthByOpenId(String openId);

    /**
     * 注册本平台的微信账号
     * @param wechatAuth
     * @return
     * @throws WechatAuthOperationException
     */
    public WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;

}
