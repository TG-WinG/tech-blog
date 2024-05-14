package kr.tgwing.tech.common.exception;

import kr.tgwing.tech.blog.exception.*;
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
        mapper.put(PostNotFoundException.class,
                ExceptionSituation.of("해당하는 블로그를 찾을 수 없습니다", HttpStatus.NOT_FOUND, 4400));
        mapper.put(PathHasNoPostIdException.class,
                ExceptionSituation.of("경로에 적절한 블로그의 id가 필요합니다", HttpStatus.BAD_REQUEST, 4401));
        mapper.put(PageExceededDataException.class,
                ExceptionSituation.of("데이터 갯수에 초과되는 페이지 수가 입력되었습니다", HttpStatus.BAD_REQUEST, 4402));
        mapper.put(InappropriateUserPostRelationException.class,
                ExceptionSituation.of("블로그 작성자 정보가 서로 일치하지 않습니다", HttpStatus.CONFLICT, 4403));
        mapper.put(UserIsNotPostWriterException.class,
                ExceptionSituation.of("블로그 수정(삭제) 권한이 존재하지 않습니다", HttpStatus.FORBIDDEN, 4404));
        mapper.put(UserNotFoundException.class,
                ExceptionSituation.of("해당하는 유저를 찾을 수 없습니다", HttpStatus.NOT_FOUND, 4405));
        mapper.put(WrongPostRequestException.class,
                ExceptionSituation.of("요청에 잘못된 정보가 포함되어 있습니다", HttpStatus.BAD_REQUEST, 4406));
    }


    public static ExceptionSituation getSituationOf(Exception exception) {
        return mapper.get(exception.getClass());
    }


}
