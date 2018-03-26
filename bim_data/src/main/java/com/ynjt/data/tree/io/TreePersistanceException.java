package com.ynjt.data.tree.io;

public class TreePersistanceException extends Exception{

    public TreePersistanceException() {
    }

    public TreePersistanceException(String message) {
        super(message);
    }

    public TreePersistanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TreePersistanceException(Throwable cause) {
        super(cause);
    }

    public TreePersistanceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
