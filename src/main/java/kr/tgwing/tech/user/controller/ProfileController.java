package kr.tgwing.tech.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.tgwing.tech.blog.entity.PostEntity;
import kr.tgwing.tech.common.ApiResponse;
import kr.tgwing.tech.user.dto.ProfileDTO;
import kr.tgwing.tech.user.dto.ProfileReqDTO;
import kr.tgwing.tech.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.config.RepositoryNameSpaceHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
@Tag(name = "회원정보 수정하기")
public class ProfileController {

    private final UserService userService;

    @Operation(summary = "회원정보 조회하기")
    @GetMapping("")
    public ResponseEntity<ApiResponse<ProfileDTO>> showProfile(
            Principal principal){
        String studentId = principal.getName();
        ProfileDTO profileDTO = userService.showUser(studentId);

        return ResponseEntity.ok(ApiResponse.ok(profileDTO));
    }

    @Operation(summary = "내가 쓴 글 가져오기" )
    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<PostEntity>>> showMyPost(Principal principal){
        String studentId = principal.getName();
        List<PostEntity> blog = userService.showMyBlog(studentId);

        return ResponseEntity.ok(ApiResponse.ok(blog));

    }

    @Operation(summary = "유저 정보 수정 완료" )
    @PutMapping("")
    public ResponseEntity<ApiResponse<Long>> changeProfile(
            @RequestBody ProfileReqDTO request,
            Principal principal){
        String studentId = principal.getName();
        Long change = userService.changeUser(studentId, request);

        return ResponseEntity.ok(ApiResponse.updated(change));
    }

    @Operation(summary = "회원 탈퇴" )
    @DeleteMapping("")
    public ResponseEntity<ApiResponse<Long>> removeProfile(Principal principal){
        String studentId = principal.getName();
        Long remove = userService.removeUser(studentId);

        return ResponseEntity.ok(ApiResponse.delete(remove));

    }
//    @GetMapping("/myPosting")
}
