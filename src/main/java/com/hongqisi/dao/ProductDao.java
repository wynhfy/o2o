package com.hongqisi.dao;

import com.hongqisi.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {

    public int insertProduct(Product product);

    /**
     * 通过productId查询商品信息
     * @param productId
     * @return
     */
    public Product queryProductById(long productId);

    /**
     * 修改商品信息
     * @param product
     * @return
     */
    public int updateProduct(Product product);

    /**
     * 查询商品列表并分页
     * 根据商品名、商品状态、店铺ID、商品类别查询
     * @param productCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    public List<Product> queryProductList(@Param("productCondition") Product productCondition,@Param("rowIndex")
            int rowIndex,@Param("pageSize") int pageSize);

    /**
     * 查询对应商品的总数
     * @param product
     * @return
     */
    public int queryProductCount(@Param("productCondition") Product product);

    /**
     * 删除商品类别之前，将商品类别ID置为空
     * @param productCategoryId
     * @return
     */
    public int updateProductCategoryToNull(Long productCategoryId);

}
