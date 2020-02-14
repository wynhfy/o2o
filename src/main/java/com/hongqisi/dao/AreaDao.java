package com.hongqisi.dao;

import com.hongqisi.entity.Area;

import java.util.List;

public interface AreaDao {

    /**
     * 列出区域列表
     * @return areaList
     */
    public List<Area> queryArea();

}
