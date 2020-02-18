package com.hongqisi.dto;

import com.hongqisi.entity.Product;
import com.hongqisi.enums.ProductStateEnum;

import java.util.List;

public class ProductExecution {

    private int state;
    private String stateInfo;
    private int count;
    private Product product;
    private List<Product> productList;

    public ProductExecution(){

    }
    //操作失败
    public ProductExecution(ProductStateEnum productStateEnum){
        this.state=productStateEnum.getState();
        this.stateInfo=productStateEnum.getStateInfo();
    }
    //操作成功
    public ProductExecution(ProductStateEnum productStateEnum,Product product){
        this.state=productStateEnum.getState();
        this.stateInfo=productStateEnum.getStateInfo();
        this.product=product;
    }
    public ProductExecution(ProductStateEnum productStateEnum,List<Product> list){
        this.state=productStateEnum.getState();
        this.stateInfo=productStateEnum.getStateInfo();
        this.productList=list;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
