package com.hongqisi.service;


import java.util.List;
import com.hongqisi.dao.AreaDao;
import com.hongqisi.entity.Area;
import org.springframework.beans.factory.annotation.Autowired;

public interface AreaService {

     public List<Area> getAreaList();

}
