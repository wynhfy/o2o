package com.hongqisi.dto;

import com.hongqisi.entity.ProductCategory;
import com.hongqisi.enums.ProductCategoryStateEnum;

import java.util.List;

public class ProductCategoryExecution {

    private int state;

    private String stateInfo;

    private List<ProductCategory> productCategoryList;

    //成功
    public ProductCategoryExecution(ProductCategoryStateEnum productCategoryStateEnum){
        this.state=productCategoryStateEnum.getState();
        this.stateInfo=productCategoryStateEnum.getStateInfo();
    }

    //失败
    public ProductCategoryExecution(ProductCategoryStateEnum productCategoryStateEnum,List<ProductCategory> list){
        this.state=productCategoryStateEnum.getState();
        this.stateInfo=productCategoryStateEnum.getStateInfo();
        this.productCategoryList=list;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<ProductCategory> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }
}
