package kr.tgwing.tech.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.tgwing.tech.security.util.JwtUtil;
import kr.tgwing.tech.user.dto.LoginDTO;
import kr.tgwing.tech.user.dto.ProfileDTO;
import kr.tgwing.tech.user.dto.ProfileReqDTO;
import kr.tgwing.tech.user.dto.UserDTO;
import kr.tgwing.tech.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/test")
    public ResponseEntity<String> test(@RequestHeader("authorization") String token) {

        String s = token.split(" ")[1];
        String studentId = jwtUtil.getStudentId(s);

        return ResponseEntity.ok(studentId);
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {

        System.out.println(userDTO);

        try{
            userService.register(userDTO);

        } catch (Exception e) {

        }

        return ResponseEntity.ok("okokok");
    }

//    @PostMapping("/password/check")
//    public ResponseEntity<Void> passwordCheck(@RequestBody ) {
//
//
//
//        return ResponseEntity.ok().build();
//    }
}


