package kr.tgwing.tech.user.controller;

import java.security.Principal;
import java.util.List;

import kr.tgwing.tech.blog.dto.PostOverview;
import kr.tgwing.tech.project.dto.ProjectBriefDTO;
import kr.tgwing.tech.project.dto.ProjectQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.tgwing.tech.blog.entity.Post;
import kr.tgwing.tech.common.ApiResponse;
import kr.tgwing.tech.user.dto.profiledto.ProfileDTO;
import kr.tgwing.tech.user.dto.profiledto.ProfileReqDTO;
import kr.tgwing.tech.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
@Tag(name = "회원정보")
public class ProfileController {

    private final UserService userService;

    @Operation(summary = "회원정보 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<ProfileDTO>> showProfile(
            Principal principal){
        ProfileDTO profileDTO = userService.showUser(principal.getName());
        return ResponseEntity.ok(ApiResponse.ok(profileDTO));
    }

    @Operation(summary = "내 글 가져오기" )
    @GetMapping("/blog")
    public ResponseEntity<ApiResponse<Page<PostOverview>>> getMyPost(
            Principal principal,
            @PageableDefault Pageable pageable){
        Page<PostOverview> blog = userService.getMyBlog(principal.getName(), pageable);
        return ResponseEntity.ok(ApiResponse.ok(blog));
    }

    @Operation(summary = "회원정보 수정" )
    @PutMapping
    public ResponseEntity<ApiResponse<Long>> changeProfile(
            @RequestBody ProfileReqDTO request,
            Principal principal) {
        Long change = userService.changeUser(principal.getName(), request);
        return ResponseEntity.ok(ApiResponse.updated(change));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public ResponseEntity<ApiResponse<Long>> removeProfile(Principal principal){
        Long remove = userService.removeUser(principal.getName());
        return ResponseEntity.ok(ApiResponse.delete(remove));

    }

    @Operation(summary = "내 프로젝트 가져오기")
    @GetMapping("/myPosting")
    public ResponseEntity<?> getMyProject(
            Principal principal,
            @PageableDefault Pageable pageable,
            @ModelAttribute ProjectQuery query) {
        Page<ProjectBriefDTO> myProjects = userService.getMyProject(pageable, query, principal.getName());
        return ResponseEntity.ok(ApiResponse.created(myProjects));
    }
}
