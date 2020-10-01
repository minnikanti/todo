package com.mypack.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ToDoResponse {
    private String statusCode;
    private String message;
}
