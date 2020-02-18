package com.hongqisi.service.impl;

import com.hongqisi.BaseTest;
import com.hongqisi.dto.ImageHolder;
import com.hongqisi.dto.ShopExecution;
import com.hongqisi.entity.Area;
import com.hongqisi.entity.PersonInfo;
import com.hongqisi.entity.Shop;
import com.hongqisi.entity.ShopCategory;
import com.hongqisi.enums.ShopStateEnum;
import com.hongqisi.service.ShopService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.io.File;
import static org.junit.Assert.*;
import java.util.List;

public class ShopServiceTest extends BaseTest {

    @Autowired
    private ShopService shopService;


    @Test
    public void testQueryShopListAndCount(){
        Shop shopCondition=new Shop();
        ShopCategory shopCategory=new ShopCategory();
        shopCategory.setShopCategoryId(2L);
        shopCondition.setShopCategory(shopCategory);
        ShopExecution  se=shopService.getShopList(shopCondition,2,3);
        List<Shop> shopList=se.getShopList();
        int count=se.getCount();
        System.out.println(shopList.size());
        System.out.println(count);
    }

    @Test
    @Ignore
    public void testModifyShop() throws FileNotFoundException {
        Shop shop=new Shop();
        shop.setShopId(12L);
        shop.setPhone("17306692310");
        File file=new File("/Users/ynwu/Pictures/qiaoba.jpg");
        InputStream is=new FileInputStream(file);
        ImageHolder imageHolder=new ImageHolder(file.getName(),is);
        ShopExecution shopExecution=shopService.modifyShop(shop,imageHolder);
        System.out.println("新的图片地址为："+shopExecution.getShop().getShopImg());
    }

    @Test
    @Ignore
    public void addShopTest() throws FileNotFoundException {
        Shop shop=new Shop();
        PersonInfo owner=new PersonInfo();
        Area area=new Area();
        ShopCategory shopCategory=new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(1L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试店铺3");
        shop.setShopDesc("test3");
        shop.setShopAddr("test3");
        shop.setPhone("test3");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");
        File shopImg=new File("/Users/ynwu/pictures/qiaoba.jpg");
        InputStream is=new FileInputStream(shopImg);
        ImageHolder imageHolder=new ImageHolder(shopImg.getName(),is);
        ShopExecution se=shopService.addShop(shop,imageHolder);
        assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
    }

}