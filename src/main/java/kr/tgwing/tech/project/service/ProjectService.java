package kr.tgwing.tech.project.service;

import kr.tgwing.tech.project.dto.ProjectBriefDTO;
import kr.tgwing.tech.project.dto.ProjectCreateDTO;
import kr.tgwing.tech.project.dto.ProjectDetailDTO;
import kr.tgwing.tech.project.dto.ProjectUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

//    List<ProjectBriefDTO> getProjects();

    Page<ProjectBriefDTO> getProjectsInPage(String text, int page, Pageable pageable);

    Long createProjects(ProjectCreateDTO projectCreateDTO);

    ProjectDetailDTO getOneProject(Long project_id);

    Long updateProject(ProjectUpdateDTO projectUpdateDTO, Long project_id);

    void deleteProject(Long project_id);
}
