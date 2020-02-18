package com.hongqisi.service;

import com.hongqisi.dto.ProductCategoryExecution;
import com.hongqisi.entity.ShopCategory;
import com.hongqisi.exceptions.ProductCategoryOperationException;

import java.util.List;

public interface ShopCategoryService {

    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);


}
