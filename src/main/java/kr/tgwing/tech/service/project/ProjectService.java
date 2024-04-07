package kr.tgwing.tech.service.project;

import kr.tgwing.tech.dto.ProjectBriefDTO;
import kr.tgwing.tech.dto.ProjectCreateDTO;
import kr.tgwing.tech.dto.ProjectDetailDTO;
import kr.tgwing.tech.dto.ProjectUpdateDTO;

import java.util.List;

public interface ProjectService {

    List<ProjectBriefDTO> getProjects();

    Long createProjects(ProjectCreateDTO projectCreateDTO);

    ProjectDetailDTO getOneProject(Long project_id);

    Long updateProject(ProjectUpdateDTO projectUpdateDTO, Long project_id);

    void deleteProject(Long project_id);
}
