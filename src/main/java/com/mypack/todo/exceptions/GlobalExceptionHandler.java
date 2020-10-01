package com.mypack.todo.exceptions;

import com.mypack.todo.dto.ToDoResponse;
import com.mypack.todo.util.AppConstants;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ToDoResponse> handleAllExceptions(final Exception exception) {
        final ToDoResponse response = new ToDoResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), AppConstants.GLOBAL_ERROR_MESSAGE);
        log.info("{}, \n Exception Details: {}", response.toString(), ExceptionUtils.getStackTrace(exception));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
