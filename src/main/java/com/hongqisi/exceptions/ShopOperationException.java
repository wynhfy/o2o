package com.hongqisi.exceptions;

public class ShopOperationException extends RuntimeException {


    private static final long serialVersionUID = -9172791633394101196L;

    public ShopOperationException(String msg){
        super(msg);
    }

}
