package kr.tgwing.tech.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.tgwing.tech.common.ApiResponse;
import kr.tgwing.tech.project.dto.ProjectBriefDTO;
import kr.tgwing.tech.project.dto.ProjectCreateDTO;
import kr.tgwing.tech.project.dto.ProjectDetailDTO;
import kr.tgwing.tech.project.dto.ProjectUpdateDTO;
import kr.tgwing.tech.project.service.ProjectServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "프로젝트")
@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
@Slf4j
public class ProjectController {
    private final ProjectServiceImpl projectServiceImpl;

    @Operation(summary = "프로젝트 전체 조회")
    @GetMapping("")
    public ResponseEntity<?> getProjects(){
        List<ProjectBriefDTO> projects = projectServiceImpl.getProjects();
        return ResponseEntity.ok(ApiResponse.ok(projects));
    }

    @Operation(summary = "프로젝트 상세 조회")
    @GetMapping("/{project_id}")
    public ResponseEntity<?> getOneProject(@PathVariable("project_id") Long project_id){
        ProjectDetailDTO project = projectServiceImpl.getOneProject(project_id);
        return ResponseEntity.ok(ApiResponse.ok(project));
    }

    @Operation(summary = "프로젝트 생성")
    @PostMapping("")
    public ResponseEntity<?> postProject(
            @Valid @RequestBody ProjectCreateDTO projectCreateDTO) {
        Long projectId = projectServiceImpl.createProjects(projectCreateDTO);
        return ResponseEntity.ok(ApiResponse.created(projectId));
    }
    @Operation(summary = "프로젝트 수정")
    @PutMapping("/{project_id}")
    public ResponseEntity<?> updateProject(
            @PathVariable("project_id") Long project_id,
            @Valid @RequestBody ProjectUpdateDTO projectUpdateDTO){
        Long projectId = projectServiceImpl.updateProject(projectUpdateDTO, project_id);
        return ResponseEntity.ok(ApiResponse.updated(projectId));
    }
    @Operation(summary = "프로젝트 삭제")
    @DeleteMapping("/{project_id}")
    public ResponseEntity<?> deleteProject(@PathVariable("project_id") Long project_id){
        projectServiceImpl.deleteProject(project_id);
        return ResponseEntity.ok(ApiResponse.delete(project_id));
    }



}
