package kr.tgwing.tech.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ExceptionSituation {
    private final String message;
    private final HttpStatus statusCode;
    private final int errorCode;

    public static ExceptionSituation of(String message, HttpStatus statusCode, int errorCode) {
        return new ExceptionSituation(message, statusCode, errorCode);
    }
}
