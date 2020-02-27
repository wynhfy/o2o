package com.hongqisi.dao;

import com.hongqisi.entity.LocalAuth;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface LocalAuthDao {

    /**
     * 根据账号和密码查询对应信息，登陆的时候使用
     * @param username
     * @param password
     * @return
     */
    public LocalAuth queryByUserNameAndPwd(@Param("username") String username, @Param("password") String password);

    /**
     * 根据用户userId查询对应信息
     * @param userId
     * @return
     */
    public LocalAuth queryByUserId(@Param("userId") long userId);

    /**
     * 添加平台账号
     * @param localAuth
     * @return
     */
    public int insertLocalAuth(LocalAuth localAuth);

    public int updateLocalAuth(@Param("userId") long userId, @Param("username") String username, @Param("password") String password,
                               @Param("newPassword") String newPassword, @Param("lastEditTime")Date lastEditTime);
}
