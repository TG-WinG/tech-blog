package kr.tgwing.tech.project.advice;

import kr.tgwing.tech.project.dto.ErrorResultDTO;
import kr.tgwing.tech.project.exception.BadRequestException;
import kr.tgwing.tech.project.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
public class ProjectControllerAdvice {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResultDTO> notFoundExHandler(NotFoundException e){
        ErrorResultDTO errorResult = new ErrorResultDTO("NOT_FOUND", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResultDTO> badRequestException(BadRequestException e) {
        ErrorResultDTO errorResult = new ErrorResultDTO("BAD_REQUEST", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ResponseEntity<ErrorResultDTO> baseException(Exception e){
        ErrorResultDTO errorResult = new ErrorResultDTO("INTERNAL_SERVER_ERROR", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
