package com.hongqisi.service;

import com.hongqisi.dto.LocalAuthExecution;
import com.hongqisi.entity.LocalAuth;
import com.hongqisi.exceptions.LocalAuthOperationException;

public interface LocalAuthService {


    public LocalAuth queryByUserNameAndPwd(String username,String password) throws LocalAuthOperationException;

    /**
     * 主要用于判断此用户是否已经绑定过平台账号
     * @param userId
     * @return
     * @throws LocalAuthOperationException
     */
    public LocalAuth queryByUserId(Long userId) throws LocalAuthOperationException;

    /**
     * 绑定微信账号
     * @param localAuth
     * @return
     * @throws LocalAuthOperationException
     */
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

    /**
     * 主要用于修改密码
     * @param userId
     * @param username
     * @param password
     * @param newpassword
     * @return
     * @throws LocalAuthOperationException
     */
    public LocalAuthExecution updateLocalAuth(Long userId,String username,String password,String newpassword) throws LocalAuthOperationException;

}
