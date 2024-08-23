package kr.tgwing.tech.project.service;

import kr.tgwing.tech.project.domain.Link;
import kr.tgwing.tech.project.domain.Participant;
import kr.tgwing.tech.project.domain.Project;
import kr.tgwing.tech.project.dto.*;
import kr.tgwing.tech.project.exception.ProjectNotFoundException;
import kr.tgwing.tech.project.repository.ParticipantRepository;
import kr.tgwing.tech.project.repository.LinkRepository;
import kr.tgwing.tech.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl{

    private final ProjectRepository projectRepository;
    private final ParticipantRepository participantRepository;
    private final LinkRepository linkRepository;

    public List<ProjectBriefDTO> getProjects() {
        List<Project> projects = projectRepository.findAll();
        if(projects == null){
            return null;
        } else {
            return projects.stream()
                    .map(ProjectServiceImpl::entity2ProjectBriefDTO)
                    .toList();
        }
    }

    public Long createProjects(ProjectCreateDTO projectCreateDTO) {
        List<Participant> participantList = projectCreateDTO.getParticipants().stream()
                .map(ParticipantDTO::toParticipantEntity)
                .toList();
        for(Participant participant: participantList){
            participantRepository.save(participant);
        }

        List<Link> linkList = projectCreateDTO.getLinks().stream()
                .map(LinkDTO::toLinkEntity)
                .toList();
        for(Link link: linkList){
            linkRepository.save(link);
        }

        Project saveProject = projectRepository
                .save(ProjectCreateDTO.toEntity(projectCreateDTO));

        for(Participant participant: participantList){
            participant.setProject(saveProject);
            participantRepository.save(participant);
        }
        for(Link link: linkList){
            link.setProject(saveProject);
            linkRepository.save(link);
        }
        return saveProject.getId();
    }

    @Transactional
    public ProjectDetailDTO getOneProject(Long project_id) {
        Project project = projectRepository.findById(project_id)
                .orElseThrow(ProjectNotFoundException::new);

        return ProjectServiceImpl.entity2ProjectDetailDTO(project);
    }

    @Transactional
    public Long updateProject(ProjectUpdateDTO projectUpdateDTO, Long project_id) {
        Project findProject = projectRepository.findById(project_id)
                .orElseThrow(ProjectNotFoundException::new);
        findProject.updateProject(projectUpdateDTO);
        findProject.setParticipants(projectUpdateDTO.getParticipants());
        findProject.setLinks(projectUpdateDTO.getLinks());
        return findProject.getId();
    }

    public void deleteProject(Long project_id) {
        Project findProject = projectRepository.findById(project_id)
                .orElseThrow(ProjectNotFoundException::new);
        projectRepository.deleteById(project_id);
    }

    //--------------------------------------
    public static ProjectBriefDTO entity2ProjectBriefDTO(Project project){
        return ProjectBriefDTO.builder()
                .id(project.getId())
                .title(project.getTitle())
                .start(project.getStartDate())
                .end(project.getEndDate())
                .thumbnail(project.getThumbnail())
                .devStatus(project.getDevStatus())
                .devType(project.getDevType())
                .build();
    }

    public static ProjectDetailDTO entity2ProjectDetailDTO(Project project){
        return ProjectDetailDTO.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .start(project.getStartDate())
                .end(project.getEndDate())
                .thumbnail(project.getThumbnail())
                .devStatus(project.getDevStatus())
                .devType(project.getDevType())
                .participants(project.getParticipants().stream()
                        .map(Participant::toDTO).toList())
                .links(project.getLinks().stream()
                        .map(Link::toDTO).toList())
                .build();
    }


}
