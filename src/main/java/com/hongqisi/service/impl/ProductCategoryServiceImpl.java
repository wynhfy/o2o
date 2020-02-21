package com.hongqisi.service.impl;

import com.hongqisi.dao.ProductCategoryDao;
import com.hongqisi.dao.ProductDao;
import com.hongqisi.dto.ProductCategoryExecution;
import com.hongqisi.entity.ProductCategory;
import com.hongqisi.enums.ProductCategoryStateEnum;
import com.hongqisi.exceptions.ProductCategoryOperationException;
import com.hongqisi.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public List<ProductCategory> getProductCategoryList(long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Override
    @Transactional
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) {
        if(productCategoryList!=null&&productCategoryList.size()!=0){
            try{
                int effectedNum=productCategoryDao.batchInsertProductCategory(productCategoryList);
                if(effectedNum<=0){
                    throw new ProductCategoryOperationException("店铺类别创建失败");
                }else{
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            }catch(Exception e){
                throw new ProductCategoryOperationException("batchAddProductCategory error:"+e.getMessage());
            }
        }else{
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException {
        //TODO 将该商品类型下的商品的商品类型置为空
        //解除了tb_product里的商品与productCategoryId的关联
        try{
            int effectedNum=productDao.updateProductCategoryToNull(productCategoryId);
            if(effectedNum<0){
                throw new RuntimeException("商品类别更新失败");
            }
        }catch (Exception e){
            throw new RuntimeException("deleteProductCategory error:"+e.getMessage());
        }
        try{
            int effectedNum=productCategoryDao.deleteProductCategory(productCategoryId,shopId);
            if(effectedNum<=0){
                throw new ProductCategoryOperationException("商品类别删除失败");
            }else{
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        }catch(Exception e){
            throw new ProductCategoryOperationException("deleteProductCategory error:"+e.getMessage());
        }
    }
}
