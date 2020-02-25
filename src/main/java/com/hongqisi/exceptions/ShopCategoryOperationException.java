package com.hongqisi.exceptions;

public class ShopCategoryOperationException extends RuntimeException {
    private static final long serialVersionUID = -3107437660033214073L;
    public ShopCategoryOperationException(String msg){
        super(msg);
    }
}
