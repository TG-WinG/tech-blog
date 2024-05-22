package kr.tgwing.tech.common.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    // 기본적인 로그를 처리하기 위한 로거
    Logger defaultLog = LoggerFactory.getLogger(ExceptionAdvice.class);
    // 예외가 발생했을 때의 로그를 기록하기 위한 별도 로거
    Logger exceptionLog = LoggerFactory.getLogger("ExceptionLogger");

    // GifthubException 을 처리하는 메서드로, ExceptionMapper 를 사용하여 예외 상황을 가져오고 로그 기록 & 적절한 응답 반환
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ExceptionResponse> handleCommonException(CommonException e) {
        ExceptionSituation exceptionSituation = ExceptionMapper.getSituationOf(e);
        defaultLog.warn(exceptionSituation.getMessage());
        exceptionLog.warn(exceptionSituation.getMessage(), e);
        return ResponseEntity.status(exceptionSituation.getStatusCode())
                .body(ExceptionResponse.from(exceptionSituation));
    }

    // 일반 적인 에러에 대해 server error 500을 보냄
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleCommonException(Exception e) {
        log.info("~~~~~~~~~~~~~~~~~~~~");
        defaultLog.error(e.getMessage());
        exceptionLog.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().build();
    }
}
