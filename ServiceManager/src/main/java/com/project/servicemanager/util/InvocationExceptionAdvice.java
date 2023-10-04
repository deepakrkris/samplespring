package com.project.servicemanager.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class InvocationExceptionAdvice {
    @ExceptionHandler(InvocationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInvocationException(InvocationException e) {
        return new HashMap<>() {
            {
                put("err", e.getMessage());
                put("localized", e.getLocalizedMessage());
            }
        };
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUnknownException(Exception e) {
        return new HashMap<>() {
            {
                put("unknownErr", e.getMessage());
                put("localized", e.getLocalizedMessage());
            }
        };
    }
}
