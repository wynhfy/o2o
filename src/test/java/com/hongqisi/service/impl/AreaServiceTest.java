package com.hongqisi.service.impl;

import com.hongqisi.BaseTest;
import com.hongqisi.entity.Area;
import com.hongqisi.service.AreaService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import static org.junit.Assert.*;

public class AreaServiceTest extends BaseTest {

    @Autowired
    private AreaService areaService;

    @Test
    public void getAreaListTest(){
        List<Area> list=areaService.getAreaList();
        assertEquals("西苑",list.get(0).getAreaName());
    }

}