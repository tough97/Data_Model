package com.ynjs.data.excp;

public class TreeNodeConflictException extends Exception{
    public TreeNodeConflictException() {
    }

    public TreeNodeConflictException(String message) {
        super(message);
    }

    public TreeNodeConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public TreeNodeConflictException(Throwable cause) {
        super(cause);
    }

    public TreeNodeConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
