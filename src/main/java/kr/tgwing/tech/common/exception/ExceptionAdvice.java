package kr.tgwing.tech.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ExceptionAdvice { // Exception Handler
    Logger defaultLog = LoggerFactory.getLogger(ExceptionAdvice.class);
    Logger exceptionLog = LoggerFactory.getLogger("ExceptionLogger");

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ExceptionResponse> handleCommonException(CommonException e) {
        ExceptionSituation exceptionSituation = ExceptionMapper.getSituationOf(e);

        defaultLog.warn(exceptionSituation.getMessage());
        exceptionLog.warn(exceptionSituation.getMessage(), e);

        return ResponseEntity.status(exceptionSituation.getStatusCode())
                .body(ExceptionResponse.from(exceptionSituation));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        if (e instanceof NoResourceFoundException) {
            defaultLog.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        defaultLog.error(e.getMessage());
        exceptionLog.error(e.getMessage(), e);

        return ResponseEntity.internalServerError().build();
    }
}
