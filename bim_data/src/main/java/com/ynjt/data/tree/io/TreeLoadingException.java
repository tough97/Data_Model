package com.ynjt.data.tree.io;

public class TreeLoadingException extends Exception{

    public TreeLoadingException() {
    }

    public TreeLoadingException(String message) {
        super(message);
    }

    public TreeLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TreeLoadingException(Throwable cause) {
        super(cause);
    }

    public TreeLoadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
