package com.hongqisi.service;

import com.hongqisi.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {

    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);

}
