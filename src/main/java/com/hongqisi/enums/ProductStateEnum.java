package com.hongqisi.enums;

public enum ProductStateEnum {

    SUCCESS(1,"添加成功"),EMPTY(0,"商品信息为空");

    private int state;
    private String stateInfo;

    private ProductStateEnum(int state,String stateInfo){
        this.state=state;
        this.stateInfo=stateInfo;
    }

    public int getState() {
        return state;
    }

    public static ProductStateEnum stateOf(int state){
        for(ProductStateEnum productStateEnum:values()){
            if(productStateEnum.getState()==state){
                return productStateEnum;
            }
        }
        return null;
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
}
