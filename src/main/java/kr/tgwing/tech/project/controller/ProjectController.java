package kr.tgwing.tech.project.controller;

import jakarta.validation.Valid;
import kr.tgwing.tech.project.dto.ProjectBriefDTO;
import kr.tgwing.tech.project.dto.ProjectCreateDTO;
import kr.tgwing.tech.project.dto.ProjectDetailDTO;
import kr.tgwing.tech.project.dto.ProjectUpdateDTO;
import kr.tgwing.tech.project.exception.*;
import kr.tgwing.tech.project.service.ProjectServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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
    public ResponseEntity<?> getOneProject(@PathVariable("project_id") Long project_id) {
        ProjectDetailDTO project = projectServiceImpl.getOneProject(project_id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            authorities.forEach(authority -> {
                System.out.println("User Authority: " + authority.getAuthority());
            });
        }

        return ResponseEntity.ok(project);
    }

    @PostMapping("/projects")
    public ResponseEntity<?> postProject(@Valid @RequestBody ProjectCreateDTO projectCreateDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new BadPostRequestException();
        }
        Long projectId = projectServiceImpl.createProjects(projectCreateDTO);
        return ResponseEntity.ok(projectId);
    }

    @PutMapping("/projects/{project_id}")
    public ResponseEntity<?> updateProject(@PathVariable("project_id") Long project_id, @Valid @RequestBody ProjectUpdateDTO projectUpdateDTO, BindingResult bindingResult) throws ProjectNotFoundException, BadRequestException{
        if(bindingResult.hasErrors()){
            throw new BadUpdateRequestException();
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
