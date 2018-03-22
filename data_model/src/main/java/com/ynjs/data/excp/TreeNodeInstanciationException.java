package com.ynjs.data.excp;

public class TreeNodeInstanciationException extends Exception{
    public TreeNodeInstanciationException() {
    }

    public TreeNodeInstanciationException(String message) {
        super(message);
    }

    public TreeNodeInstanciationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TreeNodeInstanciationException(Throwable cause) {
        super(cause);
    }

    public TreeNodeInstanciationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
