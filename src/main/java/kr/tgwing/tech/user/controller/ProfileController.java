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
        String studentId = principal.getName();
        ProfileDTO profileDTO = userService.showUser(studentId);

        return ResponseEntity.ok(profileDTO);
    }

    @PutMapping("/change")
    public ResponseEntity<Long> changeProfile(@RequestBody ProfileReqDTO request, Principal principal){
        String studentId = principal.getName();
        Long change = userService.changeUser(studentId, request);

        return ResponseEntity.ok(change); //요거가 response다시
    }


//    @GetMapping("/myPosting")


}
