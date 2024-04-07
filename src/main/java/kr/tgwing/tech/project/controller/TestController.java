package kr.tgwing.tech.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestController {

    @GetMapping("/join")
    public String joinGET(){
        return "joinController";
    }

    @GetMapping("/join2")
    public String joinGET2(){
        return "joinController2";
    }
}
