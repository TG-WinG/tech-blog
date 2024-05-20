package kr.tgwing.tech.common.exception;

import kr.tgwing.tech.blog.exception.*;
import kr.tgwing.tech.reply.exception.*;
import kr.tgwing.tech.user.exception.*;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExceptionMapper { // 예외 객체 -> 예외 상태로 바꿔주는 mapper

    private static final Map<Class<? extends Exception>, ExceptionSituation> mapper = new LinkedHashMap<>();

    static {
        setUpUserException();
        setUpPostException();
        setUpReplyException();
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
        mapper.put(WrongPostRequestException.class,
                ExceptionSituation.of("요청에 잘못된 정보가 포함되어 있습니다", HttpStatus.BAD_REQUEST, 4406));
    }
    private static void setUpReplyException() {
        mapper.put(CantFindWriterException.class,
                ExceptionSituation.of("요청된 댓글의 작성자를 찾을 수 없습니다", HttpStatus.NOT_FOUND, 5400));
        mapper.put(ReplyCantNullableException.class,
                ExceptionSituation.of("댓글 내용이 존재하지 않습니다", HttpStatus.BAD_REQUEST, 5401));
        mapper.put(UserIsNotReplyWriterException.class,
                ExceptionSituation.of("댓글 삭제 권한이 존재하지 않습니다", HttpStatus.FORBIDDEN, 5402));
        mapper.put(InappropriatePostReplyRelationException.class,
                ExceptionSituation.of("요청된 댓글과 블로그가 상응하지 않습니다", HttpStatus.CONFLICT, 5403));
        mapper.put(RepliesNotFoundException.class,
                ExceptionSituation.of("댓글을 찾을 수 없습니다", HttpStatus.NOT_FOUND, 5404));
    }


    public static ExceptionSituation getSituationOf(Exception exception) {
        return mapper.get(exception.getClass());
    }


}
