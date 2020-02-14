package com.hongqisi.enums;

public enum ShopStateEnum {

    CHECK(0,"审核中"),OFFLINE(-1,"非法店铺"),
    SUCCESS(1,"操作成功"),PASS(2,"通过认证"),
    INNER_ERROR(-1001,"内部系统错误"),NULL_SHOPID(-1002,"ShopId为空");

    private int state;
    private String stateinfo;

    //设置为私有的，不希望第三方程序来改变enum值
    private ShopStateEnum(int state,String stateinfo){
        this.state=state;
        this.stateinfo=stateinfo;
    }

    /**
     * 依据传入的state返回相应的enum值
     */
    public static ShopStateEnum stateOf(int state){
        for(ShopStateEnum stateEnum:values()){
            if(stateEnum.getState()==state){
                return stateEnum;
            }
        }
        return null;
    }


    public int getState() {
        return state;
    }
    public String getStateinfo() {
        return stateinfo;
    }
}
