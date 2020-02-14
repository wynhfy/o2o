package com.hongqisi.dao;

import java.util.List;
import com.hongqisi.BaseTest;
import com.hongqisi.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class AreaDaoTest extends BaseTest {

   @Autowired
   private AreaDao areaDao;

   @Test
   public void  queryAreaTest(){
       List<Area> list=areaDao.queryArea();
       assertEquals(2,list.size());
   }
}