package com.hongqisi.exceptions;

public class LocalAuthOperationException extends RuntimeException {

    private static final long serialVersionUID = 763912088086629900L;

    public LocalAuthOperationException(String msg){
        super(msg);
    }

}
