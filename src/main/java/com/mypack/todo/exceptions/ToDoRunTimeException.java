package com.mypack.todo.exceptions;

public class ToDoRunTimeException extends RuntimeException {
    public ToDoRunTimeException(String message) {
        super(message);
    }

    public ToDoRunTimeException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ToDoRunTimeException(Throwable throwable) {
        super(throwable);
    }
}
