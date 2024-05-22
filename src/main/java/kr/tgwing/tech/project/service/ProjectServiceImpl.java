package kr.tgwing.tech.project.service;

import jakarta.transaction.Transactional;
import kr.tgwing.tech.project.domain.LinkEntity;
import kr.tgwing.tech.project.domain.OutsiderParticipantEntity;
import kr.tgwing.tech.project.domain.ParticipantEntity;
import kr.tgwing.tech.project.domain.ProjectEntity;
import kr.tgwing.tech.project.dto.*;
import kr.tgwing.tech.project.exception.DateOrderViolationException;
import kr.tgwing.tech.project.exception.ProjectNotFoundException;
import kr.tgwing.tech.project.respository.participant.OutsiderParticipantRepository;
import kr.tgwing.tech.project.respository.participant.ParticipantRepository;
import kr.tgwing.tech.project.respository.link.LinkRepository;
import kr.tgwing.tech.project.respository.project.ProjectRepository;
import kr.tgwing.tech.user.entity.OutsiderEntity;
import kr.tgwing.tech.user.entity.UserEntity;
import kr.tgwing.tech.user.repository.OutsiderEntityRepository;
import kr.tgwing.tech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final ParticipantRepository participantRepository;
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final OutsiderParticipantRepository outsiderParticipantRepository;
    private final OutsiderEntityRepository outsiderEntityRepository;

    @Override
    public List<ProjectBriefDTO> getProjects() {
        List<ProjectEntity> projects = projectRepository.findAll();
        if(projects != null){
            return projects.stream()
                    .map(ProjectServiceImpl::entity2ProjectBriefDTO)
                    .toList();
        }
        return null;
    }
    @Override
    public ProjectDetailDTO getOneProject(Long project_id) {
        Optional<ProjectEntity> findProject = projectRepository.findById(project_id);
        ProjectEntity projectEntity = findProject.orElseThrow(ProjectNotFoundException::new);
        return ProjectServiceImpl.entity2ProjectDetailDTO(projectEntity);
    }

    @Override
    public Long createProjects(ProjectCreateDTO projectCreateDTO) {
        if (projectCreateDTO.getStart().isAfter(projectCreateDTO.getEnd())) {
            throw new DateOrderViolationException();
        }
        // 새로운 프로젝트 생성 및 초기화
        ProjectEntity projectEntity = projectCreateDTO2ProjectEntity(projectCreateDTO);
        ProjectEntity participantedProject = projectRepository.saveAndFlush(projectEntity);

        List<ParticipantEntity> participants = new ArrayList<>();
        List<OutsiderParticipantEntity> outsiderParticipants = new ArrayList<>();

        for (ParticipantDTO participantDTO : projectCreateDTO.getParticipantDTOS()) {
            Optional<UserEntity> currentUser = userRepository.findByStudentId(participantDTO.getStudentId());
            if(currentUser.isPresent()){
                UserEntity userEntity = currentUser.get();
                ParticipantEntity participant = ParticipantEntity.builder()
                        .project(participantedProject)
                        .devRole(participantDTO.getDevRole())
                        .userEntity(userEntity)
                        .major(participantDTO.getMajor())
                        .build();
                participants.add(participant);
            }else{
                Optional<OutsiderEntity> outsiderEntity = outsiderEntityRepository.findByStudentId(participantDTO.getStudentId());
                if(outsiderEntity.isPresent()){
                    OutsiderParticipantEntity outsiderParticipant = OutsiderParticipantEntity.builder()
                            .project(participantedProject)
                            .devRole(participantDTO.getDevRole())
                            .outsiderEntity(outsiderEntity.get())
                            .major(participantDTO.getMajor())
                            .build();
                    outsiderParticipants.add(outsiderParticipant);

                }else{
                    OutsiderEntity newOutsider = OutsiderEntity.builder()
                            .name(participantDTO.getName())
                            .studentId(participantDTO.getStudentId())
                            .build();
                    outsiderEntityRepository.saveAndFlush(newOutsider);
                    OutsiderParticipantEntity outsiderParticipant = OutsiderParticipantEntity.builder()
                            .project(participantedProject)
                            .devRole(participantDTO.getDevRole())
                            .outsiderEntity(newOutsider)
                            .major(participantDTO.getMajor())
                            .build();
                    outsiderParticipants.add(outsiderParticipant);
                }

            }

        }
        participantRepository.saveAll(participants);
        outsiderParticipantRepository.saveAll(outsiderParticipants);


        // 링크 저장
        List<LinkEntity> links = new ArrayList<>();
        for (LinkDTO linkDto : projectCreateDTO.getLinkDTOS()) {
            LinkEntity link = LinkEntity.builder()
                    .url(linkDto.getUrl())
                    .description(linkDto.getDescription())
                    .project(participantedProject)
                    .build();
            links.add(link);
        }

        linkRepository.saveAll(links);

        return participantedProject.getId();

    }

    @Override
    public Long updateProject(ProjectUpdateDTO projectUpdateDTO, Long projectId) {
        if (projectUpdateDTO.getStart().isAfter(projectUpdateDTO.getEnd())) {
            throw new DateOrderViolationException();
        }

        Optional<ProjectEntity> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            ProjectEntity project = optionalProject.get();

            // 링크의 모든 필드 수정
            project.updateProjectByProjectUpdateDTO(projectUpdateDTO);

            // participants 수정
            updateParticipants(project, projectUpdateDTO.getParticipantDTOS());

            // links 수정
            updateLinks(project, projectUpdateDTO.getLinkDTOS());

            projectRepository.saveAndFlush(project);

            return project.getId();
        }

        return null;
    }

    protected void updateParticipants(ProjectEntity project, List<ParticipantDTO> participantDTOs) {
        participantRepository.deleteByProjectId(project.getId());
        participantRepository.flush();
        outsiderParticipantRepository.deleteByProjectId(project.getId());
        participantRepository.flush();
        outsiderEntityRepository.deleteByProjectId(project.getId());
        participantRepository.flush();

        List<ParticipantEntity> participants = new ArrayList<>();
        List<OutsiderParticipantEntity> outsiderParticipants = new ArrayList<>();
        for (ParticipantDTO participantDTO : participantDTOs) {
            Optional<UserEntity> optionalUser = userRepository.findByStudentId(participantDTO.getStudentId());
            if (optionalUser.isPresent()) {
                UserEntity user = optionalUser.get();
                ParticipantEntity participant = ParticipantEntity.builder()
                        .project(project)
                        .devRole(participantDTO.getDevRole())
                        .userEntity(user)
                        .major(participantDTO.getMajor())
                        .build();
                participants.add(participant);
            } else {
                Optional<OutsiderEntity> optionalOutsider = outsiderEntityRepository.findByStudentId(participantDTO.getStudentId());
                if(optionalOutsider.isPresent()){

                    OutsiderEntity outsider = optionalOutsider.get();
                    outsider.updateName(participantDTO.getName());

                    OutsiderParticipantEntity outsiderParticipant = OutsiderParticipantEntity.builder()
                            .project(project)
                            .devRole(participantDTO.getDevRole())
                            .outsiderEntity(optionalOutsider.get())
                            .major(participantDTO.getMajor())
                            .build();
                    outsiderParticipants.add(outsiderParticipant);

                }else{
                    OutsiderEntity newOutsider = OutsiderEntity.builder()
                            .name(participantDTO.getName())
                            .studentId(participantDTO.getStudentId())
                            .build();
                    outsiderEntityRepository.saveAndFlush(newOutsider);

                    OutsiderParticipantEntity outsiderParticipant = OutsiderParticipantEntity.builder()
                            .project(project)
                            .devRole(participantDTO.getDevRole())
                            .outsiderEntity(newOutsider)
                            .major(participantDTO.getMajor())
                            .build();
                    outsiderParticipants.add(outsiderParticipant);
                }
            }
        }
        outsiderParticipantRepository.saveAll(outsiderParticipants);
        participantRepository.saveAll(participants);

        outsiderEntityRepository.deleteOutsidersNotInAnyProject();
    }

    protected void updateLinks(ProjectEntity project, List<LinkDTO> linkDTOs) {
        // 이전에 저장된 모든 링크 삭제
        linkRepository.deleteByProjectId(project.getId());
        linkRepository.flush();
        // 새로운 링크 생성 및 저장
        List<LinkEntity> links = new ArrayList<>();
        for (LinkDTO linkDto : linkDTOs) {
            LinkEntity link = new LinkEntity();
            link.updateLink(linkDto.getProject(), linkDto.getUrl(), linkDto.getDescription());
            links.add(link);
        }
        linkRepository.saveAll(links);
    }


    @Override
    public void deleteProject(Long project_id) {
        Optional<ProjectEntity> findProject = projectRepository.findById(project_id);
        if (findProject.isPresent()){
            ProjectEntity project = findProject.get();
            projectRepository.delete(project);
            projectRepository.flush();
        }else{
            throw new ProjectNotFoundException();
        }
        outsiderEntityRepository.deleteOutsidersNotInAnyProject();
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


    public ProjectEntity projectCreateDTO2ProjectEntity(ProjectCreateDTO projectCreateDTO){

        return ProjectEntity.builder()
                .title(projectCreateDTO.getTitle())
                .description(projectCreateDTO.getDescription())
                .start(projectCreateDTO.getStart())
                .end(projectCreateDTO.getEnd())
                .thumbnail(projectCreateDTO.getThumbnail())
                .devStatus(projectCreateDTO.getDevStatus())
                .devType(projectCreateDTO.getDevType())
                .build();
    }

}
