package com.hongqisi.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongqisi.cache.JedisUtil;
import com.hongqisi.dao.ShopCategoryDao;
import com.hongqisi.dto.ProductCategoryExecution;
import com.hongqisi.entity.ShopCategory;
import com.hongqisi.exceptions.ProductCategoryOperationException;
import com.hongqisi.exceptions.ShopCategoryOperationException;
import com.hongqisi.service.ShopCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    private static Logger logger= LoggerFactory.getLogger(ShopCategoryServiceImpl.class);

    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;

    @Autowired
    private ShopCategoryDao shopCategoryDao;


    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        List<ShopCategory> list=null;
        String key=SCLISTKEY;;
        ObjectMapper mapper=new ObjectMapper();
        if(shopCategoryCondition==null){
            key=key+"_allfirstlevel";
        }else if(shopCategoryCondition!=null && shopCategoryCondition.getParent()!=null
                && shopCategoryCondition.getParent().getShopCategoryId()!=null){
            key=key+"_parent"+shopCategoryCondition.getParent().getShopCategoryId();
        }else{
            key=key+"_allsecondlevel";
        }
        if(!jedisKeys.exists(key)){
            list=shopCategoryDao.queryShopCategory(shopCategoryCondition);
            String jsonstr;
            try{
                jsonstr=mapper.writeValueAsString(list);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }
            jedisStrings.set(key,jsonstr);
        }else{
            String jsonstr=jedisStrings.get(key);
            JavaType javaType=mapper.getTypeFactory().constructParametricType(ArrayList.class,ShopCategory.class);
            try{
                list=mapper.readValue(jsonstr,javaType);
            } catch (JsonParseException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            } catch (JsonMappingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }
        }
        return list;
    }


}
