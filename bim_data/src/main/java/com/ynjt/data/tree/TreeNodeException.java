package com.ynjt.data.tree;

public class TreeNodeException extends Exception{

    public TreeNodeException() {
    }

    public TreeNodeException(String message) {
        super(message);
    }

    public TreeNodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TreeNodeException(Throwable cause) {
        super(cause);
    }

    public TreeNodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
