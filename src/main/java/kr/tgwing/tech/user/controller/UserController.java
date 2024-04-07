package kr.tgwing.tech.user.controller;

import kr.tgwing.tech.user.dto.LoginDTO;
import kr.tgwing.tech.user.dto.UserDTO;
import kr.tgwing.tech.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<String> test(Principal principal) {

        String name = principal.getName();

        return ResponseEntity.ok(name);
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO request) {
        UserDTO login = userService.login(request);
        //반환값?
//        if(login != null){
//            return ResponseEntity.ok().build(); //요거가 response다시
//        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @PostMapping("/user/logout")
    public ResponseEntity<String> logout(){
        //
        return ResponseEntity.ok("로그아웃되었습니다.");
    }

//    @PutMapping("/profile")
//    public ResponseEntity<> changeProfile(@PathVariable Long id, @RequestBody profileDTO request){
//        UserDTO change = userService.changeUser(id, request);
//        if(change != null){
//            return ResponseEntity.ok().build(); //요거가 response다시
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }

}

