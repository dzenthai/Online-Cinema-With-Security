package org.online.cinema.data.exceptionhandler;

import org.online.cinema.data.dto.exception.ExceptionInfoDTO;
import org.online.cinema.data.exception.UserInfoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MainExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> mainExceptionHandler(RuntimeException ex) {
        ExceptionInfoDTO dto = new ExceptionInfoDTO();
        dto.setInfo(ex.getMessage());
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Object> mainExceptionHandler(UserInfoException ex) {
        ExceptionInfoDTO dto = new ExceptionInfoDTO();
        dto.setInfo(ex.getMessage());
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}
