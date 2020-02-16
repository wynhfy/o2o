package com.hongqisi.service;

import com.hongqisi.dto.ShopExecution;
import com.hongqisi.entity.Shop;
import com.hongqisi.exceptions.ShopOperationException;

import java.io.File;
import java.io.InputStream;

public interface ShopService {

    /**
     * 通过id查询店铺
     * @param shopId
     * @return
     */
    public Shop getShopById(long shopId);

    /**
     * 编辑店铺信息
     * @param shop
     * @param shopImgInputStream
     * @param filename
     * @return
     * @throws ShopOperationException
     */
    public ShopExecution modifyShop(Shop shop,InputStream shopImgInputStream,String filename)throws ShopOperationException;

    /**
     * 增加一个店铺
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     * @throws ShopOperationException
     */
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName) throws ShopOperationException;


    /**
     * 根据shopCondition分页返回店铺列表
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);

}
