package kr.tgwing.tech.common.exceptions;

import kr.tgwing.tech.project.exception.*;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExceptionMapper {
    private static final Map<Class<? extends Exception>, ExceptionSituation> mapper = new LinkedHashMap<Class<? extends Exception>, ExceptionSituation>();

    static {
        setUpProjectException();
    }
    private static void setUpProjectException() {
        mapper.put(BadRequestException.class,
                ExceptionSituation.of("잘못된 정보 입력", HttpStatus.BAD_REQUEST, 1201));
        mapper.put(BadPostRequestException.class,
                ExceptionSituation.of("프로젝트 생성 - 잘못된 정보 입력", HttpStatus.BAD_REQUEST, 1202));
        mapper.put(BadUpdateRequestException.class,
                ExceptionSituation.of("프로젝트 수정 - 잘못된 정보 입력", HttpStatus.BAD_REQUEST, 1203));
        mapper.put(ProjectNotFoundException.class,
                ExceptionSituation.of("해당 id값에 해당하는 프로젝트를 찾을 수 없습니다", HttpStatus.NOT_FOUND, 1204));
        mapper.put(DateOrderViolationException.class,
                ExceptionSituation.of("입력한 날짜의 전후 관계가 잘못 되었습니다", HttpStatus.BAD_REQUEST, 1205));

    }
    // 해당 예외 클래스에 대한 ExceptionSituation 반환
    public static ExceptionSituation getSituationOf(Exception exception) {
        return mapper.get(exception.getClass());
    }

    // 모든 예외 상황을 담는 리스트 반환
    public static List<ExceptionSituation> getExceptionSituations() {
        return mapper.values()
                .stream()
                .toList();
    }
}
