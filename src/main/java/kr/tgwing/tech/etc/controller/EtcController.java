package kr.tgwing.tech.etc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EtcController {
    @GetMapping("/health")
    public String getHealth() {
        return "I'm healthy😀";
    }
}
