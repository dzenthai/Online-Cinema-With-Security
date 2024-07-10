package org.online.cinema.common.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.online.cinema.common.exception.*;
import org.online.cinema.common.dto.ExceptionInfoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class MainExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> mainExceptionHandler(RuntimeException ex) {
        ExceptionInfoDTO dto = new ExceptionInfoDTO();
        dto.setInfo(ex.getMessage());
        log.error("Unnamed exception: exception={}, status={}", ex.getMessage(),
                HttpStatus.NOT_FOUND.getReasonPhrase());
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Object> mainExceptionHandler(UserInfoException ex) {
        ExceptionInfoDTO dto = new ExceptionInfoDTO();
        dto.setInfo(ex.getMessage());
        log.error("User info exception: exception={}, status={}", ex.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase());
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> mainExceptionHandler(UserException ex) {
        ExceptionInfoDTO dto = new ExceptionInfoDTO();
        dto.setInfo(ex.getMessage());
        log.error("User exception: exception={}, status={}", ex.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase());
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> mainExceptionHandler(ActorException ex) {
        ExceptionInfoDTO dto = new ExceptionInfoDTO();
        dto.setInfo(ex.getMessage());
        log.error("Actor exception: exception={}, status={}", ex.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase());
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> mainExceptionHandler(MovieException ex) {
        ExceptionInfoDTO dto = new ExceptionInfoDTO();
        dto.setInfo(ex.getMessage());
        log.error("Movie exception: exception={}, status={}", ex.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase());
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> mainExceptionHandler(RatingException ex) {
        ExceptionInfoDTO dto = new ExceptionInfoDTO();
        dto.setInfo(ex.getMessage());
        log.error("Rating exception: exception={}, status={}", ex.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase());
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}
