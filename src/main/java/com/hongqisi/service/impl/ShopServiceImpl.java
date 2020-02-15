package com.hongqisi.service.impl;

import com.hongqisi.dao.ShopDao;
import com.hongqisi.dto.ShopExecution;
import com.hongqisi.entity.Shop;
import com.hongqisi.enums.ShopStateEnum;
import com.hongqisi.exceptions.ShopOperationException;
import com.hongqisi.service.ShopService;
import com.hongqisi.util.ImageUtil;
import com.hongqisi.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    /**
     * 1、空值判断
     * 2、给店铺初始化
     * 3、添加店铺信息
     * 4、存储图片
     * 5、更新店铺图片地址
     * @param shop
     * @param shopImgInputstream
     * @return
     */
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, InputStream shopImgInputstream,String filename) throws ShopOperationException{
        if(shop==null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try{
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            int effectedNum=shopDao.insertShop(shop);
            if(effectedNum<=0){
                throw new ShopOperationException("店铺创建失败");
            }else{
                if(shopImgInputstream !=null){
                    try{
                        addShopImg(shop, shopImgInputstream,filename);
                    }catch (Exception e){
                        throw new ShopOperationException("addShopImg error:"+e.getMessage());
                    }
                    effectedNum=shopDao.updateShop(shop);
                    if(effectedNum<=0){
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }
        }catch(Exception e){
            throw new ShopOperationException("addShop error:"+e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }

    private void addShopImg(Shop shop,InputStream shopImgInputStream,String filename){
        String dest= PathUtil.getShopImgPath(shop.getShopId());
        String shopImgAddr= ImageUtil.generateThumbnail(shopImgInputStream,filename,dest);
        shop.setShopImg(shopImgAddr);
    }
}
