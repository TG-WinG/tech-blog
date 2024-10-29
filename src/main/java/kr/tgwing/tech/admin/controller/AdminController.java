package kr.tgwing.tech.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.tgwing.tech.admin.dto.AdminCheckUserDto;
import kr.tgwing.tech.admin.dto.AllStudentsDto;
import kr.tgwing.tech.admin.service.AdminService;
import kr.tgwing.tech.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "관리자")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "동아리 가입요청 목록 조회")
    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<AdminCheckUserDto>>> checkAllAssignments(
            @PageableDefault Pageable pageable) {
        Page<AdminCheckUserDto> allAssignments = adminService.checkAssingments(pageable);
        return ResponseEntity.ok(ApiResponse.ok(allAssignments));
    }

    @Operation(summary = "가입요청 수락")
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> registerAssignment(@PathVariable("id") Long id) {
        Long registerId = adminService.registerUsers(id);

        return ResponseEntity.ok(ApiResponse.updated(registerId));
    }

    @Operation(summary = "가입요청 거부")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> refuseAssignment(@PathVariable("id") Long id) {
        Long refusedId = adminService.refuseUsers(id);

        return ResponseEntity.ok(ApiResponse.delete(refusedId));
    }

    @Operation(summary = "동아리원 목록 조회")
    @GetMapping("/student")
    public ResponseEntity<ApiResponse<?>> checkAllStudents(@PageableDefault Pageable pageable) {
        Page<AllStudentsDto> allStudents = adminService.checkAllStudents(pageable);
        return ResponseEntity.ok(ApiResponse.ok(allStudents));
    }

    @Operation(summary = "동아리원 강제 삭제")
    @DeleteMapping("/stdent/{id}")
    public ResponseEntity<ApiResponse<?>> deleteStudent(@PathVariable Long id) {
        adminService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.ok(id));
    }
}
