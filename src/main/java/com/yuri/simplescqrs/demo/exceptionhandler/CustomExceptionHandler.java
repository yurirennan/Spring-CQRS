package com.yuri.simplescqrs.demo.exceptionhandler;

import com.yuri.simplescqrs.demo.command.exceptions.DeleteUserException;
import com.yuri.simplescqrs.demo.command.exceptions.UpdateUserException;
import com.yuri.simplescqrs.demo.events.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UpdateUserException.class)
    public ResponseEntity<?> handleUpdateException(UpdateUserException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DeleteUserException.class)
    public ResponseEntity<?> handleDeleteException(DeleteUserException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
