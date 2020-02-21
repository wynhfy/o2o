package com.hongqisi.dao;

import com.hongqisi.BaseTest;
import com.hongqisi.entity.Shop;
import com.hongqisi.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static org.junit.Assert.*;

public class ShopCategoryDaoTest extends BaseTest {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    @Ignore
    public void testQueryShopCategory(){
//        List<ShopCategory> list=shopCategoryDao.queryShopCategory(new ShopCategory());
//        assertEquals(2,list.size());
//        ShopCategory testshopCategory=new ShopCategory();
//        ShopCategory parentShopCategory=new ShopCategory();
//        parentShopCategory.setShopCategoryId(1L);
//        testshopCategory.setParent(parentShopCategory);
//        list=shopCategoryDao.queryShopCategory(testshopCategory);
//        assertEquals(1,list.size());
//        System.out.println(list.get(0).getShopCategoryName());
        List<ShopCategory> list=shopCategoryDao.queryShopCategory(null);
        assertEquals(3,list.size());

    }

}