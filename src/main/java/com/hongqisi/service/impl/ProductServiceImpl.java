package com.hongqisi.service.impl;

import com.hongqisi.dao.ProductDao;
import com.hongqisi.dao.ProductImgDao;
import com.hongqisi.dto.ImageHolder;
import com.hongqisi.dto.ProductExecution;
import com.hongqisi.entity.Product;
import com.hongqisi.entity.ProductImg;
import com.hongqisi.enums.ProductStateEnum;
import com.hongqisi.exceptions.ProductOperationException;
import com.hongqisi.service.ProductService;
import com.hongqisi.util.ImageUtil;
import com.hongqisi.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    /**
     * 1、处理缩略图，获取缩略图相对路径并赋值给product
     * 2、往tb_product写入商品信息，并获取productId
     * 3、结合productId批量处理商品详情图
     * 4、将商品详情图列表批量插入tb_product_img中
     * @param product
     * @param imageHolder
     * @param imageHolderList
     * @return
     * @throws ProductOperationException
     */
    @Override
    @Transactional
    public ProductExecution addShop(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList) throws ProductOperationException {
        if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null){
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            product.setEnableStatus(1);
            if(imageHolder!=null){
                addThumbnail(product,imageHolder);
            }
            try{
                int effectedNum=productDao.insertProduct(product);
                if(effectedNum<=0){
                    throw new ProductOperationException("创建商品失败");
                }
            }catch (Exception e){
                throw new ProductOperationException("创建商品失败："+e.getMessage());
            }
            if(imageHolderList!=null&&imageHolderList.size()!=0){
                addProductImgList(product,imageHolderList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS);
        }else{
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 添加图片
     * @param product
     * @param imageHolder
     */
    public void addThumbnail(Product product,ImageHolder imageHolder){
        String dest= PathUtil.getShopImgPath(product.getShop().getShopId());
        String thumbnailAddr= ImageUtil.generateThumbnail(imageHolder,dest);
        product.setImgAddr(thumbnailAddr);
    }

    /**
     * 批量添加商品图片
     * @param product
     * @param imageHolderList
     */
    public void addProductImgList(Product product, List<ImageHolder> imageHolderList){
        String dest=PathUtil.getShopImgPath(product.getShop().getShopId());
        List<ProductImg> productImgList=new ArrayList<>();
        for(ImageHolder imageHolder:imageHolderList){
            String thumbnailAddr =ImageUtil.generateNormalImg(imageHolder,dest);
            ProductImg productImg=new ProductImg();
            productImg.setProductId(product.getProductId());
            productImg.setImgAddr(thumbnailAddr);
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }
        if(productImgList.size()>0){
            try{
                int effectedNum=productImgDao.batchInsertProductImg(productImgList);
                if(effectedNum<=0){
                    throw new ProductOperationException("创建商品详情图片失败");
                }
            }catch(Exception e){
                throw new ProductOperationException("创建商品详情图片失败"+e.getMessage());
            }
        }
    }

}
