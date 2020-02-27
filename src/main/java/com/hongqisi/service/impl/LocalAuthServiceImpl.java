package com.hongqisi.service.impl;

import com.hongqisi.dao.LocalAuthDao;
import com.hongqisi.dto.LocalAuthExecution;
import com.hongqisi.entity.LocalAuth;
import com.hongqisi.enums.LocalAuthStateEnum;
import com.hongqisi.exceptions.LocalAuthOperationException;
import com.hongqisi.service.LocalAuthService;
import com.hongqisi.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.provider.MD5;

import java.util.Date;

@Service
public class LocalAuthServiceImpl  implements LocalAuthService {

    @Autowired
    private LocalAuthDao localAuthDao;


    @Override
    public LocalAuth queryByUserNameAndPwd(String username, String password) throws LocalAuthOperationException {
        return localAuthDao.queryByUserNameAndPwd(username, MD5Util.getMd5(password));
    }

    @Override
    public LocalAuth queryByUserId(Long userId) throws LocalAuthOperationException {
        return localAuthDao.queryByUserId(userId);
    }

    /**
     * 1、空值判断
     * 2、查看是否绑定过平台账号
     * 3、插入账号
     * @param localAuth
     * @return
     * @throws LocalAuthOperationException
     */
    @Override
    @Transactional
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
        if(localAuth==null || localAuth.getUsername()==null || localAuth.getPassword()==null || localAuth.getPersonInfo()==null
           || localAuth.getPersonInfo().getUserId()==null){
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        LocalAuth tempLocalAuth=localAuthDao.queryByUserId(localAuth.getPersonInfo().getUserId());
        if(tempLocalAuth!=null){
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
        }
        try{
            localAuth.setCreatetime(new Date());
            localAuth.setLastEditTime(new Date());
            localAuth.setPassword(MD5Util.getMd5(localAuth.getPassword()));
            int effectedNum=localAuthDao.insertLocalAuth(localAuth);
            if(effectedNum<=0){
                throw new LocalAuthOperationException("账号绑定失败");
            }else{
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,localAuth);
            }
        }catch (Exception e){
            throw new LocalAuthOperationException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public LocalAuthExecution updateLocalAuth(Long userId, String username, String password, String newpassword) throws LocalAuthOperationException {
        if(userId!=null&&username!=null&&password!=null&&newpassword!=null&&!password.equals(newpassword)){
            try{
                int effectedNum=localAuthDao.updateLocalAuth(userId,username,MD5Util.getMd5(password), MD5Util.getMd5(newpassword),new Date());
                if(effectedNum<=0){
                    throw new LocalAuthOperationException("修改密码失败");
                }
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            }catch (Exception e){
                throw new LocalAuthOperationException("修改密码失败:"+e.getMessage());
            }
        }else{
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
    }
}
