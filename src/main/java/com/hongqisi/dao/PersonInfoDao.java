package com.hongqisi.dao;

import com.hongqisi.entity.PersonInfo;

public interface PersonInfoDao {

    /**
     * 通过Id查询personInfo
     * @param userId
     * @return
     */
    public PersonInfo queryPersonInfoById(long userId);


    /**
     * 添加用户信息
     * @param personInfo
     * @return
     */
    public int insertPersonInfo(PersonInfo personInfo);

}
