package kr.tgwing.tech.service.project;

import kr.tgwing.tech.domain.project.LinkEntity;
import kr.tgwing.tech.domain.project.ParticipantEntity;
import kr.tgwing.tech.domain.project.ProjectEntity;
import kr.tgwing.tech.dto.*;
import kr.tgwing.tech.respository.link.LinkRepository;
import kr.tgwing.tech.respository.participant.ParticipantRepository;
import kr.tgwing.tech.respository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final ParticipantRepository participantRepository;
    private final LinkRepository linkRepository;

    @Override
    public List<ProjectBriefDTO> getProjects() {
        List<ProjectEntity> projects = projectRepository.findAll();
        if(projects == null){
            return null;
        }else{
            return projects.stream()
                    .map(ProjectServiceImpl::entity2ProjectBriefDTO)
                    .toList();
        }
    }

    @Override
    public Long createProjects(ProjectCreateDTO projectCreateDTO) {
        List<ParticipantEntity> participantList = projectCreateDTO.getParticipants().stream()
                .map(ParticipantDTO::toParticipantEntity)
                .toList();
        for(ParticipantEntity participant: participantList){
            participantRepository.save(participant);
        }

        List<LinkEntity> linkList = projectCreateDTO.getLinks().stream()
                .map(LinkDTO::toLinkEntity)
                .toList();
        for(LinkEntity link: linkList){
            linkRepository.save(link);
        }

        ProjectEntity saveProject = projectRepository.save(ProjectCreateDTO.toEntity(projectCreateDTO));

        for(ParticipantEntity participant: participantList){
            participant.setProject(saveProject);
            participantRepository.save(participant);
        }
        for(LinkEntity link: linkList){
            link.setProject(saveProject);
            linkRepository.save(link);
        }
        return saveProject.getId();
    }

    @Override
    public ProjectDetailDTO getOneProject(Long project_id) {
        Optional<ProjectEntity> findProject = projectRepository.findById(project_id);
        if(findProject.isPresent()){
            ProjectEntity project = findProject.get();
            return ProjectServiceImpl.entity2ProjectDetailDTO(project);
        }
        return null;

    }

    @Override
    public Long updateProject(ProjectUpdateDTO projectUpdateDTO, Long project_id) {
        Optional<ProjectEntity> findProject = projectRepository.findById(project_id);
        if (findProject.isPresent()){
            ProjectEntity project = findProject.get();
            projectRepository.save(project);
            return project.getId();
        }
        return null;
    }

    @Override
    public void deleteProject(Long project_id) {
        Optional<ProjectEntity> findProject = projectRepository.findById(project_id);
        if (findProject.isPresent()){
            ProjectEntity project = findProject.get();
            projectRepository.delete(project);
        }
    }

    //--------------------------------------
    public static ProjectBriefDTO entity2ProjectBriefDTO(ProjectEntity projectEntity){
        return ProjectBriefDTO.builder()
                .title(projectEntity.getTitle())
                .start(projectEntity.getStart())
                .end(projectEntity.getEnd())
                .thumbnail(projectEntity.getThumbnail())
                .devStatus(projectEntity.getDevStatus())
                .devType(projectEntity.getDevType())
                .build();
    }

    public static ProjectDetailDTO entity2ProjectDetailDTO(ProjectEntity projectEntity){
        return ProjectDetailDTO.builder()
                .title(projectEntity.getTitle())
                .description(projectEntity.getDescription())
                .start(projectEntity.getStart())
                .end(projectEntity.getEnd())
                .thumbnail(projectEntity.getThumbnail())
                .devStatus(projectEntity.getDevStatus())
                .devType(projectEntity.getDevType())
                .participants(projectEntity.getParticipants())
                .links(projectEntity.getLinks())
                .build();
    }


}
