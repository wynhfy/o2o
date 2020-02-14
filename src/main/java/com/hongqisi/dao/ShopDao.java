package com.hongqisi.dao;

import com.hongqisi.entity.Shop;

public interface ShopDao {

    /**
     * 增加店铺
     * @param shop
     * @return
     */
    public int insertShop(Shop shop);

    /**
     * 修改店铺
     * @param shop
     * @return
     */
    public int updateShop(Shop shop);

}
