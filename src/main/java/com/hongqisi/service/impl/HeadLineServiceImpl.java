package com.hongqisi.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongqisi.cache.JedisUtil;
import com.hongqisi.dao.HeadLineDao;
import com.hongqisi.entity.HeadLine;
import com.hongqisi.exceptions.AreaOperationException;
import com.hongqisi.exceptions.HeadLineOperationException;
import com.hongqisi.service.HeadLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {

    private static Logger logger= LoggerFactory.getLogger(HeadLineServiceImpl.class);

    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;
    @Autowired
    private HeadLineDao headLineDao;


    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {

        String key=HEADLINELIST;
        ObjectMapper mapper=new ObjectMapper();
        List<HeadLine> headLineList=null;
        if(headLineCondition!=null && headLineCondition.getEnableStatus()!=null){
           key=key+"_"+headLineCondition.getEnableStatus();
        }
        if(!jedisKeys.exists(key)){
            String jsonstr=null;
            headLineList=headLineDao.queryHeadLine(headLineCondition);
            try{
                jsonstr=mapper.writeValueAsString(headLineList);
            }catch (JsonProcessingException e){
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }
            jedisStrings.set(key,jsonstr);
        }else{
            String jsonstr=jedisStrings.get(key);
            JavaType javaType=mapper.getTypeFactory().constructParametricType(ArrayList.class,HeadLine.class);
            try{
                headLineList=mapper.readValue(jsonstr,javaType);
            }catch (JsonParseException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            } catch (JsonMappingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }
        }
        return headLineList;
    }

}
