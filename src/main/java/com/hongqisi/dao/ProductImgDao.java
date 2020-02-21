package com.hongqisi.dao;

import com.hongqisi.entity.ProductImg;
import java.util.List;

public interface ProductImgDao {

    /**
     * 批量添加商品图片
     * @param productImgList
     * @return
     */
    public int batchInsertProductImg(List<ProductImg> productImgList);


    /**
     * 删除指定商品下的所有详情图
     * @param productId
     * @return
     */
    public int deleteProductImgByProductId(Long productId);

    /**
     * 根据商品id查询详情图
     * @param productId
     * @return
     */
    public List<ProductImg> queryProductImgList(long productId);
}
