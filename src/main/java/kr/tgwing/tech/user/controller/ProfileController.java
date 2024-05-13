package kr.tgwing.tech.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.tgwing.tech.common.ApiResponse;
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
@Tag(name = "회원정보 수정하기")
public class ProfileController {

    private final UserService userService;

    @Operation(summary = "회원정보 조회하기 + 수정페이지 입력부분")
    @GetMapping(value = {"", "/fix"})
    public ResponseEntity<ApiResponse<ProfileDTO>> showProfile(Principal principal){
        String studentId = principal.getName();
        ProfileDTO profileDTO = userService.showUser(studentId);

        return ResponseEntity.ok(ApiResponse.ok(profileDTO));
    }

    @Operation(summary = "유저 정보 수정 완료" )
    @PutMapping("/fix")
    public ResponseEntity<ApiResponse<Long>> changeProfile(@RequestBody ProfileReqDTO request, Principal principal){
        String studentId = principal.getName();
        Long change = userService.changeUser(studentId, request);

        return ResponseEntity.ok(ApiResponse.updated(change));
    }


//    @GetMapping("/myPosting")


}
