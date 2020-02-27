package com.hongqisi.enums;

public enum LocalAuthStateEnum {
    ONLY_ONE_ACCOUNT(-1007,"最多只能绑定一个账号"),LOGINFAIL(-1,"账号或密码输入错误"),
    SUCCESS(1,"操作成功"),NULL_AUTH_INFO(-1006,"注册信息为空");

    private int state;
    private String stateInfo;

    private LocalAuthStateEnum(int state,String stateInfo){
        this.state=state;
        this.stateInfo=stateInfo;
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

    public static LocalAuthStateEnum valueOf(int index){
        for(LocalAuthStateEnum stateEnum:values()){
            if(stateEnum.getState()==index){
                return stateEnum;
            }
        }
        return null;
    }
}
