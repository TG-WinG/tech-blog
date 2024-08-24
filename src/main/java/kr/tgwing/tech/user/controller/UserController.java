package kr.tgwing.tech.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.tgwing.tech.common.ApiResponse;
import kr.tgwing.tech.security.service.JwtBlackListService;
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


@Tag(name = "회원가입/비밀번호 확인")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtBlackListService jwtBlackListService;

    @Operation(summary = "회원가입 1단계: 인증코드 이메일로 전송")
    @PostMapping("/register/email")
    public ResponseEntity<ApiResponse<String>> register1(@RequestBody EmailDto emailDto) {
        EmailMessageDTO emailMessageDTO = emailDto.toRegister1(emailDto);

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String emailKey = UUID.randomUUID().toString();
        valueOperations.set(emailKey, userService.sendEmail(emailMessageDTO));

        return ResponseEntity.ok(ApiResponse.ok(emailKey));
    }

    @Operation(summary = "회원가입 2단계: 인증코드 확인")
    @PostMapping("/register/check")
    public ResponseEntity<ApiResponse<String>> register2(
            @RequestParam("emailKey") String emailKey,
            @RequestBody CheckNumberDTO checkNumberDTO) {

        String code = redisTemplate.opsForValue().get(emailKey);
        userService.checkCode(code, checkNumberDTO);

        return ResponseEntity.ok(ApiResponse.ok("이메일이 인증되었습니다."));
    }

    @Operation(summary = "회원가입 3단계 :회원 등록(임시회원)")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Long>> register(@RequestBody UserDTO userDTO) {
        Long userId = userService.register(userDTO);
        return ResponseEntity.ok(ApiResponse.created(userId));
    }

    @Operation(summary = "비밀번호 확인 1단계: 본인 확인")
    @PostMapping("/password")
    public ResponseEntity<ApiResponse<Pair<String, String>>> checkUser(
            @RequestBody CheckUserDTO checkUserDTO) {
        userService.checkUser(checkUserDTO);
        EmailMessageDTO emailMessageDTO = checkUserDTO.toCheckPassword(checkUserDTO);

        String code = userService.sendEmail(emailMessageDTO);

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String studentKey = UUID.randomUUID().toString();
        String emailKey = UUID.randomUUID().toString();

        valueOperations.set(studentKey, checkUserDTO.getStudentNumber());
        valueOperations.set(emailKey, code);

        return ResponseEntity.ok(ApiResponse.created(new Pair<>(studentKey, emailKey)));
    }

    @Operation(summary = "비밀번호 확인 2단계: 본인 이메일 인증")
    @PostMapping("/password/check")
    public ResponseEntity<ApiResponse<String>> checkNumber(
            @RequestParam("studentKey") String studentKey,
            @RequestParam("emailKey") String emailKey,
            @RequestBody CheckNumberDTO checkNumberDTO) {

        // redis로 가져와서 code의 숫자값과 입력으로 들어온 숫자가 같은지 확인하기
        String code = redisTemplate.opsForValue().get(emailKey);
        userService.checkCode(code, checkNumberDTO);

        return ResponseEntity.ok(ApiResponse.created(studentKey));
    }

    @Operation(summary = "비밀번호 확인 3단계: 비밀번호 재설정")
    @PutMapping("/password") // 쿼리 파라미터로 넘겨받기
    public ResponseEntity<ApiResponse<Long>> setNewPassword(
            @RequestParam("studentKey") String studentKey,
            @RequestBody PasswordCheckDTO passwordCheckDTO) {
        String studentId = redisTemplate.opsForValue().get(studentKey);
        Long userId = userService.setNewPassword(studentId, passwordCheckDTO);

        return ResponseEntity.ok(ApiResponse.updated(userId));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader(name = "Authorization") String authorization) {
        String token = authorization.split(" ")[1];
        jwtBlackListService.addToBlacklist(token);

        return ResponseEntity.ok(ApiResponse.ok());
    }

    @Operation(summary = "로그아웃 시, 리다이렉트(임시용)")
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}


