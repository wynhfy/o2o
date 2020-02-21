package com.hongqisi.service;

import com.hongqisi.dto.ImageHolder;
import com.hongqisi.dto.ProductExecution;
import com.hongqisi.entity.Product;
import com.hongqisi.exceptions.ProductOperationException;

import java.io.InputStream;
import java.util.List;

public interface ProductService {

    /**
     * 添加商品信息以及图片处理
     * @Param product
     * @Param imageHolder
     * @Param imageHolderList
     * @return
     * @throws ProductOperationException
     */
    public ProductExecution addShop(Product product, ImageHolder imageHolder,List<ImageHolder> imageHolderList)
        throws ProductOperationException;

    /**
     * 通过商品ID查询商品信息
     * @param productId
     * @return
     */
    public Product getProductById(Long productId);

    /**
     * 修改商品信息以及图片处理
     * @param product
     * @param thumbnail
     * @param productImgHolderList
     * @return
     * @throws ProductOperationException
     */
    public ProductExecution modifyProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgHolderList)throws ProductOperationException;


    public ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);



}
