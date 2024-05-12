package kr.tgwing.tech.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.tgwing.tech.security.util.JwtUtil;
import kr.tgwing.tech.user.dto.*;
import kr.tgwing.tech.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Log4j2
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @GetMapping("/test")
    public ResponseEntity<String> test(@RequestHeader("authorization") String token) {

        String s = token.split(" ")[1];
        String studentId = jwtUtil.getStudentId(s);

        return ResponseEntity.ok(studentId);
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {

        log.info("UserController Register...............");
        log.info(userDTO);

        try{
            userService.register(userDTO);

        } catch (Exception e) {

        }

        return ResponseEntity.ok("okokok");
    }

    // 본인 확인 페이지
    @PostMapping("/password/checkUser")
    public ResponseEntity<Pair<String, String>> checkUser(@RequestBody CheckUserDTO checkUserDTO) {
        log.info("UserController checkUser................");
        log.info(checkUserDTO);

        Boolean isExist = userService.checkUser(checkUserDTO); // user가 자신의 개인정보가 맞는지 확인

        if(!isExist)
            throw new IllegalStateException(); // 해당 회원정보는 존재하지 않습니다.

        // 맞으면 해당 메일로 인증번호 보내기
        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                        .receiver(checkUserDTO.getEmail())
                        .subject("[TGWING] Email 인증코드 발급")
                        .build();

        String code = userService.sendEmail(emailMessageDTO); // 인증코드 만듬 + 이메이 전송

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String studentKey = UUID.randomUUID().toString(); // UUID 또는 다른 고유한 값을 사용할 수 있습니다.
        String emailKey = UUID.randomUUID().toString();

        log.info(studentKey + " / " + emailKey);

        valueOperations.set(studentKey, checkUserDTO.getStudentId());
        valueOperations.set(emailKey, code);

        return ResponseEntity.ok(new Pair<>(studentKey, emailKey)); // ok
    }

    // 인증번호 확인 페이지
    @PostMapping("/password/checkNumber")
    public ResponseEntity checkNumber(@RequestParam("emailKey") String emailKey,
                                      @RequestParam("studentKey") String studentKey,
                                      @RequestBody CheckNumberDTO checkNumberDTO) {

        // redis로 가져와서 code의 숫자값과 입력으로 들어온 숫자가 같은지 확인하기
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object code = valueOperations.get(emailKey);


        if(!code.equals(checkNumberDTO.getCode()))
            throw new IllegalStateException(); // 인증코드가 일치하지 않습니다.

        return ResponseEntity.ok(new Pair<>(emailKey, studentKey));
    }

    // 비밀번호 재설정 페이지
    @PostMapping("/password/setPassword") // 쿼리 파라미터로 넘겨받기
    public ResponseEntity setNewPassword(@RequestParam("studentKey") String studentKey,
                                         @RequestBody PasswordCheckDTO passwordCheckDTO) {

        log.info("UserController setNewPassword................");
        log.info(passwordCheckDTO);

        //redis로 studentId를 가져와서 학번으로 사람 구분하기
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object studentId = valueOperations.get(studentKey);

        userService.setNewPassword(studentId, passwordCheckDTO);

        return ResponseEntity.ok().build();
    }

}


