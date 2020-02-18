package com.hongqisi.exceptions;

public class ProductOperationException extends RuntimeException {

    private static final long serialVersionUID = -830948257198271560L;

    public ProductOperationException(String msg){
        super(msg);
    }

}
