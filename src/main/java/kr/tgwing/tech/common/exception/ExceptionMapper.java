package kr.tgwing.tech.common.exception;

import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExceptionMapper { // 예외 객체 -> 예외 상태로 바꿔주는 mapper

    private static final Map<Class<? extends Exception>, ExceptionSituation> mapper = new LinkedHashMap<>();

    static {
        setUpUserException();
        setUpPostException();
//        setUpReplyException();
    }

    private static void setUpUserException() {
    }

    private static void setUpPostException() {
    }


    public static ExceptionSituation getSituationOf(Exception exception) {
        return mapper.get(exception.getClass());
    }


}
