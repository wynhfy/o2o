package com.hongqisi.service.impl;

import com.hongqisi.dao.ShopCategoryDao;
import com.hongqisi.dto.ProductCategoryExecution;
import com.hongqisi.entity.ShopCategory;
import com.hongqisi.exceptions.ProductCategoryOperationException;
import com.hongqisi.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        return shopCategoryDao.queryShopCategory(shopCategoryCondition);
    }


}
