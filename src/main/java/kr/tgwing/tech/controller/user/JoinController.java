package kr.tgwing.tech.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class JoinController {

    @GetMapping("/join")
    public String joinGET(){
        return "joinController";
    }

    @GetMapping("/join2")
    public String joinGET2(){
        return "joinController2";
    }
}
