package kr.tgwing.tech.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class UserController {
    @GetMapping("/test")
    public ResponseEntity<String> test(Principal principal) {

        String name = principal.getName();

        return ResponseEntity.ok(name);
    }
}
