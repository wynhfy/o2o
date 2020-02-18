package com.hongqisi.exceptions;

public class ProductCategoryOperationException extends RuntimeException {

    private static final long serialVersionUID = -8478300976415742912L;

    public ProductCategoryOperationException(String msg){
        super(msg);
    }
}
