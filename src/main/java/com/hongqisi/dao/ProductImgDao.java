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

}
