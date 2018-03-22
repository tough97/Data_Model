package com.ynjs.data.excp;

public class IllegalJSONFormatException extends Exception{

    public IllegalJSONFormatException() {
    }

    public IllegalJSONFormatException(String message) {
        super(message);
    }

    public IllegalJSONFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalJSONFormatException(Throwable cause) {
        super(cause);
    }

    public IllegalJSONFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
