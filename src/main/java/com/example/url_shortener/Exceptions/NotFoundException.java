package com.example.url_shortener.Exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by Sherif.Abdulraheem 12/19/2025 - 3:50 PM
 **/
public class NotFoundException extends RuntimeException {

    private final transient Object[] args;

    private final HttpStatus status;

    public NotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
        args = new Object[]{};
    }

    public NotFoundException(Object[] args) {
        this.args = args;
        this.status = HttpStatus.NOT_FOUND;
    }

    public NotFoundException(String message, Object[] args, HttpStatus status) {
        super(message);
        this.args = args;
        this.status = status;
    }

    public Object[] getArgs() {
        return args;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }
}
