package com.hongqisi.web.superadmin;

import com.hongqisi.entity.Area;
import com.hongqisi.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("/superadmin")
public class AreaController {

    Logger logger= LoggerFactory.getLogger(AreaController.class);

    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/listarea" ,method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listArea(){
        logger.info("===start===");
        long starttime=System.currentTimeMillis();
        //HashMap的key唯一
        Map<String,Object> modelMap=new HashMap<String,Object>();
        List<Area> list=new ArrayList<Area>();
        try{
            list=areaService.getAreaList();
            modelMap.put("rows",list);
            modelMap.put("total",list.size());
        }catch(Exception e){
            modelMap.put("success",false);
            modelMap.put("msg",e.getMessage());
        }
        logger.error("test errot!");
        long endtime=System.currentTimeMillis();
        logger.debug("costtime:[{}ms]",endtime-starttime);
        logger.info("===end===");
        return modelMap;
    }

}
