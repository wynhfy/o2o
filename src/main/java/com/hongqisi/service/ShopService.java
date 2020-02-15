package com.hongqisi.service;

import com.hongqisi.dto.ShopExecution;
import com.hongqisi.entity.Shop;
import com.hongqisi.exceptions.ShopOperationException;

import java.io.File;
import java.io.InputStream;

public interface ShopService {

    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName) throws ShopOperationException;

}
