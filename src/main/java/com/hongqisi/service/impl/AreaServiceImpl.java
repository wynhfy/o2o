package com.hongqisi.service.impl;

import com.hongqisi.dao.AreaDao;
import com.hongqisi.entity.Area;
import com.hongqisi.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;

    @Override
    public List<Area> getAreaList() {
          return areaDao.queryArea();
    }
}
