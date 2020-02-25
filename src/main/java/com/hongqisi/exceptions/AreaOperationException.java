package com.hongqisi.exceptions;

public class AreaOperationException extends RuntimeException {

    private static final long serialVersionUID = 2139701893767983955L;

    public AreaOperationException(String msg){
        super(msg);
    }
}
