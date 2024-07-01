package org.online.cinema.common.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.online.cinema.common.exception.UserException;
import org.online.cinema.common.dto.ExceptionInfoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> securityExceptionHandler(UserException ex) {
        ExceptionInfoDTO dto = new ExceptionInfoDTO();
        dto.setInfo(ex.getMessage());
        log.error("Security exception: exception={}, status={}", ex.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase());
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}
