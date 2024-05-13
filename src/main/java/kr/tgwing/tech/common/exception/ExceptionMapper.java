package kr.tgwing.tech.common.exception;

import kr.tgwing.tech.user.exception.*;
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
        mapper.put(UserNotFoundException.class,
                ExceptionSituation.of("해당 사용자가 존재하지 않습니다.", HttpStatus.NOT_FOUND, 1000));
        mapper.put(UserDuplicatedException.class,
                ExceptionSituation.of("해당 사용자의 정보가 중복됩니다.", HttpStatus.CONFLICT, 1001));
        mapper.put(PasswordException.class,
                ExceptionSituation.of("비밀번호가 서로 일치하지 않습니다.", HttpStatus.BAD_REQUEST,1002));
        mapper.put(MessageException.class,
                ExceptionSituation.of("메일에서 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, 1003));
        mapper.put(EmailCodeException.class,
                ExceptionSituation.of("인증번호가 서로 인치하지 않습니다.", HttpStatus.CONFLICT, 1004));
    }

    private static void setUpPostException() {
    }


    public static ExceptionSituation getSituationOf(Exception exception) {
        return mapper.get(exception.getClass());
    }


}
