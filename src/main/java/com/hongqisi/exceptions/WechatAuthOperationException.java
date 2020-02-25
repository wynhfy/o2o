package com.hongqisi.exceptions;

public class WechatAuthOperationException extends RuntimeException {

    private static final long serialVersionUID = 2274381450316924860L;

    public WechatAuthOperationException(String msg){
        super(msg);
    }

}
