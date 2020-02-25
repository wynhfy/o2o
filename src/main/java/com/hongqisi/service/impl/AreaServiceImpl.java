package com.hongqisi.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongqisi.cache.JedisUtil;
import com.hongqisi.dao.AreaDao;
import com.hongqisi.entity.Area;
import com.hongqisi.exceptions.AreaOperationException;
import com.hongqisi.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    private static Logger logger= LoggerFactory.getLogger(AreaServiceImpl.class);

    @Autowired
    private AreaDao areaDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;


    @Override
    @Transactional
    public List<Area> getAreaList() {
        String key=AREALISTKEY;
        List<Area> areaList=null;
        ObjectMapper mapper=new ObjectMapper();

        if(!jedisKeys.exists(key)){
            //如果redis中不存在
            areaList=areaDao.queryArea();
            String jsonstr;
            try{
                jsonstr=mapper.writeValueAsString(areaList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
            jedisStrings.set(key,jsonstr);
        }else{
            String jsonstr=jedisStrings.get(key);
            JavaType javaType=mapper.getTypeFactory().constructParametricType(ArrayList.class,Area.class);
            try{
                areaList=mapper.readValue(jsonstr,javaType);
            } catch (JsonParseException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            } catch (JsonMappingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
        }
        return areaList;
    }


}
