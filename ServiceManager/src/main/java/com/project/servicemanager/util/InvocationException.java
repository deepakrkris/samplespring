package com.project.servicemanager.util;

public class InvocationException extends Exception {
    public InvocationException(String errorMsg, String errorCode) {
        super("Error occurred code : " + errorCode + " message : " + errorMsg);
    }
}
