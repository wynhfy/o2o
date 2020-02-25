package com.hongqisi.dao;

import com.hongqisi.entity.WechatAuth;

public interface WechatAuthDao {

    /**
     * 通过openId查询对应本平台的微信账号
     * @param openId
     * @return
     */
    public WechatAuth queryWechatAuthByOpenId(String openId);

    /**
     * 添加对应本平台的微信账号
     * @param wechatAuth
     * @return
     */
    public int insertWechatAuth(WechatAuth wechatAuth);

}
