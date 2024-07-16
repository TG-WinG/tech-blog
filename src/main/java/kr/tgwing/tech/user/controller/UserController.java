package kr.tgwing.tech.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.tgwing.tech.common.ApiResponse;
import kr.tgwing.tech.security.util.JwtUtil;
import kr.tgwing.tech.user.dto.*;
import kr.tgwing.tech.user.dto.checkdto.CheckNumberDTO;
import kr.tgwing.tech.user.dto.checkdto.CheckUserDTO;
import kr.tgwing.tech.user.dto.checkdto.PasswordCheckDTO;
import kr.tgwing.tech.user.dto.registerdto.EmailDto;
import kr.tgwing.tech.user.dto.registerdto.UserDTO;
import kr.tgwing.tech.user.exception.EmailCodeException;
import kr.tgwing.tech.user.exception.MessageException;
import kr.tgwing.tech.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "로그인 하기 전 기능 + 로그아웃")
public class UserController {

    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;

    @Operation(summary = "티지윙 회원가입 1: 이메일 인증 요청하기")
    @PostMapping("/register/email")
    public ResponseEntity<ApiResponse<String>> register1(@RequestBody EmailDto emailDto) {
        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                .receiver(emailDto.getEmail())
                .subject("[TGWING] Email 인증코드 발급")
                .build();

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String emailKey = UUID.randomUUID().toString();
        String code = userService.sendEmail(emailMessageDTO);
        valueOperations.set(emailKey, code);

        return ResponseEntity.ok(ApiResponse.ok(emailKey));
    }

    @Operation(summary = "티지윙 회원가입 2: 이메일 인증 확인하기")
    @PostMapping("/register/check")
    public ResponseEntity<ApiResponse<String>> register2(
            @RequestParam("emailKey") String emailKey,
            @RequestBody CheckNumberDTO checkNumberDTO) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object code = valueOperations.get(emailKey);

        if(!code.equals(checkNumberDTO.getCode())) throw new EmailCodeException(); // 인증코드가 일치하지 않습니다.

        return ResponseEntity.ok(ApiResponse.ok("이메일이 인증되었습니다."));
    }

    @Operation(summary = "회원 등록하기")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Long>> register(@RequestBody UserDTO userDTO) {
        Long userId = userService.register(userDTO);

        return ResponseEntity.ok(ApiResponse.created(userId));
    }


    // TODO : 로그아웃 기능 만들기. 대신 제대로 좀 알고.. 지금은 단일토큰이니까 프론트에서 때달라해야하나?
//    @Operation(summary = "로그아웃하기")
//    @PostMapping("/logout")
//    public ResponseEntity<ApiResponse<Long>> logout(
//            HttpServletRequest request,
//            HttpServletResponse response) {
//        String authorization = request.getHeader("authorization");
//        String token = authorization.split(" ")[1];
//        String studentId = jwtUtil.getStudentId(token);
//
//        Long userId = userService.logout(studentId);
//        response.setHeader("authorization", null);
//
//        return ResponseEntity.ok(ApiResponse.ok(userId));
//    }

    @Operation(summary = "비밀전호 재설정하기 전, 본인확인하기")
    @PostMapping("/password")
    public ResponseEntity<ApiResponse<Pair<String, String>>> checkUser(
            @RequestBody CheckUserDTO checkUserDTO) {
        Boolean isExist = userService.checkUser(checkUserDTO);

        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                        .receiver(checkUserDTO.getEmail())
                        .subject("[TGWING] Email 인증코드 발급")
                        .build();
        String code = userService.sendEmail(emailMessageDTO);

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String studentKey = UUID.randomUUID().toString();
        String emailKey = UUID.randomUUID().toString();

        valueOperations.set(studentKey, checkUserDTO.getStudentId());
        valueOperations.set(emailKey, code);

        return ResponseEntity.ok(ApiResponse.created(new Pair<>(studentKey, emailKey)));
    }

    @Operation(summary = "본인확인 이메일 인증하기")
    @PostMapping("/password/check")
    public ResponseEntity<ApiResponse<String>> checkNumber(
            @RequestParam("studentKey") String studentKey,
            @RequestParam("emailKey") String emailKey,
            @RequestBody CheckNumberDTO checkNumberDTO) {

        // redis로 가져와서 code의 숫자값과 입력으로 들어온 숫자가 같은지 확인하기
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object code = valueOperations.get(emailKey);

        if(!code.equals(checkNumberDTO.getCode())) throw new EmailCodeException(); // 인증코드가 일치하지 않습니다.

        return ResponseEntity.ok(ApiResponse.created(studentKey));
    }

    @Operation(summary = "비밀번호 재설정하기")
    @PostMapping("/password/reset") // 쿼리 파라미터로 넘겨받기
    public ResponseEntity<ApiResponse<Long>> setNewPassword(
            @RequestParam("studentKey") String studentKey,
            @RequestBody PasswordCheckDTO passwordCheckDTO) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object studentId = valueOperations.get(studentKey);
        Long userId = userService.setNewPassword(studentId, passwordCheckDTO);

        return ResponseEntity.ok(ApiResponse.updated(userId));
    }

}


