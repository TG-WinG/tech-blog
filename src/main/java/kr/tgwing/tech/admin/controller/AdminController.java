package kr.tgwing.tech.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.tgwing.tech.admin.dto.AdminCheckUserDto;
import kr.tgwing.tech.admin.service.AdminServiceImpl;
import kr.tgwing.tech.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "관리자")
public class AdminController {

    private final AdminServiceImpl adminService;

    @Operation(summary = "요청 목록 확인")
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<AdminCheckUserDto>>> checkUsers() {
        List<AdminCheckUserDto> dtoList = adminService.checkUser();

        return ResponseEntity.ok(ApiResponse.ok(dtoList));
    }

    @Operation(summary = "회원요청 수락")
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> registerUsers(@PathVariable("id") Long id) {
        Long registerId = adminService.registerUsers(id);

        return ResponseEntity.ok(ApiResponse.updated(registerId));
    }

    @Operation(summary = "회원요청 거부")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> refuseUsers(@PathVariable("id") Long id) {
        Long refusedId = adminService.refuseUsers(id);

        return ResponseEntity.ok(ApiResponse.delete(refusedId));
    }
}
