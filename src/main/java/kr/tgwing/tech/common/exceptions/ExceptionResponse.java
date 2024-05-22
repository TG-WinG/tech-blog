package kr.tgwing.tech.common.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // json 직렬화에서 null 값은 제외
// record: 데이터를 포함하는 불변 클래스, ExceptionResponse는 code와 message 필드를 가짐
public record ExceptionResponse(Integer code, String message) {
    /*
    from 메서드는 ExceptionSituation 객체를 받아 ExceptionResponse 생성
    즉, 오류 코드와 메세지를 가져와 새로운 ExceptionResponse 객체를 생성
    이를 통해 (1)응답 형식 표준화, (2) 레코드의 불변성 유지 장점을 얻을 수 있음
     */
    public static ExceptionResponse from(ExceptionSituation exceptionSituation) {
        return new ExceptionResponse(exceptionSituation.getErrorCode(), exceptionSituation.getMessage());
    }
}