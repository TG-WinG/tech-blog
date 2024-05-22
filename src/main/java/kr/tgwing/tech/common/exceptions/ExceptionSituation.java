package kr.tgwing.tech.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter

public class ExceptionSituation {
    // ExceptionSituation - 예외 상황을 나타내는 클래스 -> exceptionResponse 의 from 통해 {code, message}로 가공됨
    private final String message;
    private final HttpStatus statusCode;
    private final Integer errorCode;

    private ExceptionSituation(String message, HttpStatus statusCode, Integer errorCode) {
        this.message = message;
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
    // ExceptionSituation 을 생성하는 정적 팩토리 메서드로 ExceptionSituation 는 of를 통해서만 생성할 수 있음
    public static ExceptionSituation of(String message, HttpStatus statusCode, Integer errorCode) {
        return new ExceptionSituation(message, statusCode, errorCode);
    }
}
