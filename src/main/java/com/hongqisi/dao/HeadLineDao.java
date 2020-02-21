package com.hongqisi.dao;

import com.hongqisi.entity.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HeadLineDao {

    /**
     * 查询头条
     * @param headLineConditon
     * @return
     */
    public List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineConditon);


}
