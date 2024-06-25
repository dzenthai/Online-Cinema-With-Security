package org.online.cinema.data.exceptionhandler;

import org.online.cinema.data.dto.exception.ExceptionInfoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> globalExceptionHandler(RuntimeException ex) {
        ExceptionInfoDTO dto = new ExceptionInfoDTO();
        dto.setInfo(ex.getMessage());
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }
}
