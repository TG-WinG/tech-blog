package kr.tgwing.tech.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionSituation {
    private final String message;
    private final HttpStatus statusCode;
    private final int errorCode; // 어느기능에서 에러가 났는지에 대한

    public static ExceptionSituation of(String message, HttpStatus statusCode, int errorCode) {
        return new ExceptionSituation(message, statusCode, errorCode);
    }
}
