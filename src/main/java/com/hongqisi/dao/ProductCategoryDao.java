package com.hongqisi.dao;

import java.util.List;
import com.hongqisi.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

public interface ProductCategoryDao {

    /**
     * 根据shopId查询该店铺的商品类型
     * @param shopId
     * @return
     */
    public List<ProductCategory> queryProductCategoryList(long shopId);


    /**
     * 批量添加商品类别
     * @param productCategoryList
     * @return
     */
    public int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    /**
     * 删除商品类别
     * @param productCategoryId
     * @param shopId
     * @return
     */
    public int deleteProductCategory(@Param("productCategoryId") long productCategoryId,@Param("shopId") long shopId);

}
