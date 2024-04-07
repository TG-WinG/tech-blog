package kr.tgwing.tech.project.controller;

import jakarta.validation.Valid;
import kr.tgwing.tech.project.dto.ProjectBriefDTO;
import kr.tgwing.tech.project.dto.ProjectCreateDTO;
import kr.tgwing.tech.project.dto.ProjectDetailDTO;
import kr.tgwing.tech.project.dto.ProjectUpdateDTO;
import kr.tgwing.tech.project.exception.BadRequestException;
import kr.tgwing.tech.project.exception.NotFoundException;
import kr.tgwing.tech.project.service.ProjectServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectServiceImpl projectServiceImpl;

    @GetMapping("/projects")
    public ResponseEntity<?> getProjects(){
        List<ProjectBriefDTO> projects = projectServiceImpl.getProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/projects/{project_id}")
    public ResponseEntity<?> getOneProject(@PathVariable("project_id") Long project_id) throws NotFoundException{
        ProjectDetailDTO project = projectServiceImpl.getOneProject(project_id);
        if(project == null){
            throw new NotFoundException("개별 프로젝트 가져오기 - 찾을 수 없음");
        }
        return ResponseEntity.ok(project);
    }


    @PostMapping("/projects")
    public ResponseEntity<?> postProject(@Valid @RequestBody ProjectCreateDTO projectCreateDTO, BindingResult bindingResult) throws BadRequestException {
        if(bindingResult.hasErrors()){
            throw new BadRequestException("프로젝트 생성 - 잘못된 정보 입력");
        }
        Long projectId = projectServiceImpl.createProjects(projectCreateDTO);
        return ResponseEntity.ok(projectId);
    }

    @PutMapping("/projects/{project_id}")
    public ResponseEntity<?> updateProject(@PathVariable("project_id") Long project_id, @Valid @RequestBody ProjectUpdateDTO projectUpdateDTO, BindingResult bindingResult) throws NotFoundException, BadRequestException{
        if(bindingResult.hasErrors()){
            throw new BadRequestException("프로젝트 수정 - 잘못된 정보 입력");
        }
        Long projectId = projectServiceImpl.updateProject(projectUpdateDTO, project_id);
        return ResponseEntity.ok(projectId);
    }

    @DeleteMapping("/projects/{project_id}")
    public ResponseEntity<?> deleteProject(@PathVariable("project_id") Long project_id){
        projectServiceImpl.deleteProject(project_id);
        return ResponseEntity.ok("delete ok");
    }



}
