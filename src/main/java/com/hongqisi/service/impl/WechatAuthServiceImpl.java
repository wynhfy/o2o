package com.hongqisi.service.impl;

import com.hongqisi.dao.PersonInfoDao;
import com.hongqisi.dao.WechatAuthDao;
import com.hongqisi.dto.WechatAuthExecution;
import com.hongqisi.entity.PersonInfo;
import com.hongqisi.entity.WechatAuth;
import com.hongqisi.enums.WechatAuthStateEnum;
import com.hongqisi.exceptions.WechatAuthOperationException;
import com.hongqisi.service.WechatAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class WechatAuthServiceImpl implements WechatAuthService {

    @Autowired
    private PersonInfoDao personInfoDao;
    @Autowired
    private WechatAuthDao wechatAuthDao;

    @Override
    public WechatAuth getWechatAuthByOpenId(String openId) {
        return wechatAuthDao.queryWechatAuthByOpenId(openId);
    }

    @Override
    @Transactional
    public WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException {
        //空值判断
        if(wechatAuth==null||wechatAuth.getOpenId()==null){
            return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
        }
        try{
            wechatAuth.setCreatetime(new Date());
            //如果该微信账号里夹带的用户信息的用户id为空，则认为该用户是第一次使用平台并且通过微信登陆
            //则自动创建用户信息
            if(wechatAuth.getPersonInfo()!=null&&wechatAuth.getPersonInfo().getUserId()==null) {
                try{
                    wechatAuth.getPersonInfo().setCreateTime(new Date());
                    wechatAuth.getPersonInfo().setEnableStatus(1);
                    PersonInfo personInfo = wechatAuth.getPersonInfo();
                    int effectedNum = personInfoDao.insertPersonInfo(personInfo);
                    wechatAuth.setPersonInfo(personInfo);
                    if (effectedNum <= 0) {
                        throw new WechatAuthOperationException("添加用户信息失败");
                    }
                }catch (Exception e){
                    throw new WechatAuthOperationException("insert PersonInfo error:"+e.getMessage());
                }
            }
            int effectedNum=wechatAuthDao.insertWechatAuth(wechatAuth);
            if(effectedNum<=0){
                throw new WechatAuthOperationException("账号创建失败");
            }else{
                return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS);
            }
        }catch(Exception e){
            throw new WechatAuthOperationException("insert wechat error："+e.getMessage());
        }

    }
}
