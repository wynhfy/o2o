package com.hongqisi.service;

import com.hongqisi.dto.ProductCategoryExecution;
import com.hongqisi.entity.ProductCategory;
import com.hongqisi.exceptions.ProductCategoryOperationException;

import java.util.List;

public interface ProductCategoryService {

    /**
     * 查询某个店铺下的商品类别信息
     * @param shopId
     * @return
     */
    public List<ProductCategory> getProductCategoryList(long shopId);

    /**
     * 批量添加商品类型
     * @param productCategoryList
     * @return
     */
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList);

    /**
     * 将该商品类别下的商品的类别的Id置为空，删除该商品类别
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws ProductCategoryOperationException
     */
    public ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId) throws ProductCategoryOperationException;

}
