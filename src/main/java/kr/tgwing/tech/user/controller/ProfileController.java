package kr.tgwing.tech.user.controller;

import kr.tgwing.tech.user.dto.ProfileDTO;
import kr.tgwing.tech.user.dto.ProfileReqDTO;
import kr.tgwing.tech.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    @GetMapping(value = {"", "/change"})
    public ResponseEntity<ProfileDTO> showProfile(Principal principal){
        String name = principal.getName();
        ProfileDTO show = userService.showUser(name);

        if(show != null){
            return ResponseEntity.ok(show);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/change")
    public ResponseEntity<Long> changeProfile(@RequestBody ProfileReqDTO request, Principal principal){
        String name = principal.getName();
        Long change = userService.changeUser(name, request);


        if(change != null){
            return ResponseEntity.ok(change); //요거가 response다시
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

//    @GetMapping("/myPosting")


}
