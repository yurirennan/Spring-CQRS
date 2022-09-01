package com.yuri.simplescqrs.demo.command.exceptions;

public class UpdateUserException extends RuntimeException {

    public UpdateUserException(String msg) {
        super(msg);
    }
}
