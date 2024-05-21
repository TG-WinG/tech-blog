package kr.tgwing.tech.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.tgwing.tech.admin.dto.AdminCheckUserDto;
import kr.tgwing.tech.admin.service.AdminServiceImpl;
import kr.tgwing.tech.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "관리자 기능")
public class AdminController {

    private final AdminServiceImpl adminService;

    @Operation(summary = "회원 요청 목록 확인하기")
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<AdminCheckUserDto>>> checkUsers() {
        List<AdminCheckUserDto> dtoList = adminService.checkUser();

        return ResponseEntity.ok(ApiResponse.ok(dtoList));
    }

    @Operation(summary = "회원 요청 수락하기")
    @PostMapping("/user")
    public ResponseEntity<ApiResponse<Long>> registerUsers() {
        List<AdminCheckUserDto> dtoList = adminService.checkUser();

        return ResponseEntity.ok(ApiResponse.ok(4L));
    }
}
