package com.yuri.simplescqrs.demo.command.exceptions;

public class DeleteUserException extends RuntimeException {

    public DeleteUserException(String msg) {
        super(msg);
    }
}
