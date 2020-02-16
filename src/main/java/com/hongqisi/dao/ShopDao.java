package com.hongqisi.dao;

import com.hongqisi.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 通过shopId查询店铺
     * @param shopId
     * @return
     */
    public Shop queryShopById(long shopId);


    /**
     * 返回queryShopList的总数
     * @param shopCondition
     * @return
     */
    public int queryShopCount(@Param("shopCondition") Shop shopCondition);

    /**
     * 分页查询店铺，可输入的条件有：店铺名(模糊)、店铺状态、店铺类别、区域Id、owner
     * @param shopCondition
     * @param rowIndex 从第几行开始取数据
     * @param pageSize 返回多少行数据
     * @return
     */
    public List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,@Param("rowIndex")
            int rowIndex,@Param("pageSize") int pageSize);

}
