package com.hongqisi.dto;

import com.hongqisi.entity.Shop;
import com.hongqisi.enums.ShopStateEnum;

import java.util.List;

public class ShopExecution {
    //结果状态
    private int state;
    //状态标识
    private String stateinfo;
    //店铺数量
    private int count;
    //操作的shop(增删改的时候用到)
    private Shop shop;
    //shop列表(查询店铺列表的时候使用)
    private List<Shop> shopList;

    public ShopExecution(){

    }
    //店铺操作失败使用的构造器
    public ShopExecution(ShopStateEnum shopStateEnum){
        this.state=shopStateEnum.getState();
        this.stateinfo=shopStateEnum.getStateinfo();
    }
    //店铺操作成功使用的构造器
    public ShopExecution(ShopStateEnum shopStateEnum,Shop shop){
        this.state=shopStateEnum.getState();
        this.stateinfo=shopStateEnum.getStateinfo();
        this.shop=shop;
    }
    //店铺操作成功使用的构造器
    public ShopExecution(ShopStateEnum shopStateEnum,List<Shop> shopList){
        this.state=shopStateEnum.getState();
        this.stateinfo=shopStateEnum.getStateinfo();
        this.shopList=shopList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateinfo() {
        return stateinfo;
    }

    public void setStateinfo(String stateinfo) {
        this.stateinfo = stateinfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }
}
