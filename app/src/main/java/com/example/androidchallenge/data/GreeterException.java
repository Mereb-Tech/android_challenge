package com.example.androidchallenge.data;

/** Domain-level failure of a greeting request. Keeps gRPC types out of the UI layer. */
public class GreeterException extends Exception {
    public GreeterException(String message, Throwable cause) {
        super(message, cause);
    }
}
