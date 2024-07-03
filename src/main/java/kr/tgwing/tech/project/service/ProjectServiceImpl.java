package kr.tgwing.tech.project.service;

import jakarta.transaction.Transactional;
import kr.tgwing.tech.project.domain.*;
import kr.tgwing.tech.project.dto.*;
import kr.tgwing.tech.project.exception.DateOrderViolationException;
import kr.tgwing.tech.project.exception.ProjectNotFoundException;
import kr.tgwing.tech.project.respository.participant.OutsiderParticipantRepository;
import kr.tgwing.tech.project.respository.participant.ParticipantRepository;
import kr.tgwing.tech.project.respository.link.LinkRepository;
import kr.tgwing.tech.project.respository.project.ProjectRepository;
import kr.tgwing.tech.project.respository.thumbnail.ThumbnailRepository;
import kr.tgwing.tech.user.entity.OutsiderEntity;
import kr.tgwing.tech.user.entity.UserEntity;
import kr.tgwing.tech.user.repository.OutsiderEntityRepository;
import kr.tgwing.tech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final ThumbnailRepository thumbnailRepository;



    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<ProjectBriefDTO> getProjectsInPage(String text, int page, Pageable pageable) {
        int size = pageable.getPageSize();
        long total = 0L;
        Page<ProjectEntity> projectPage = null;

        // Project DB에서 Page 단위로 가져오기
        PageRequest pageRequest = PageRequest.of(page, size);

        // 파라미터 없이 get 요청된 경우 - 그냥 전체 반환
        if (text == null) {
            projectPage = getAllProjects(pageRequest);
            total = projectRepository.count();
            System.out.println("total = " + total);
        } else { // 파라미터로 검색된 Project 걸러서 반환
            projectPage = searchProjects(text, pageRequest);
            total = projectRepository.countByTitleContains(text);
            System.out.println("total = " + total);
        }

        if (!(total > page * size)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // 이후 ExceptionHandler 정의 필요

        if (projectPage.isEmpty()) {
            System.out.println("조회 불가능");
        }

        // stream이 비어있는 경우
        projectPage.stream().findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));

        // page가 총 데이터 건수를 초과하는 경우
        long totalCount = projectPage.getTotalElements();
        long requestCount = (long) (projectPage.getTotalPages() - 1) * projectPage.getSize();
        if (!(totalCount > requestCount)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // Entity -> Dto 변환 - Dto 담은 page 반환
        List<ProjectEntity> posts = projectPage.getContent();
        List<ProjectBriefDTO> dtos = posts.stream()
                .map(ProjectServiceImpl::entity2ProjectBriefDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, projectPage.getTotalElements());
    }

    private Page<ProjectEntity> searchProjects(String search, PageRequest pageRequest) {
        // 공지 내용으로 검색하기
        return projectRepository.findByTitleContains(search, pageRequest);
    }

    private Page<ProjectEntity> getAllProjects(PageRequest pageRequest) {
        // 모든 공지 가져오기
        return projectRepository.findAllByOrderByIdDesc(pageRequest);
    }


    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
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
                        .name(participantDTO.getName())
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
                            .name(participantDTO.getName())
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
                            .name(participantDTO.getName())
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

        // 섬네일 저장
        List<ThumbnailEntity> thumbnails = new ArrayList<>();
        for (ThumbnailDTO thumbnailDTO : projectCreateDTO.getThumbnailDTOS()) {
            ThumbnailEntity thumbnail = ThumbnailEntity.builder()
                    .url(thumbnailDTO.getUrl())
                    .project(participantedProject)
                    .build();
            thumbnails.add(thumbnail);
        }

        thumbnailRepository.saveAll(thumbnails);

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

            // thumbnail 수정
            updateThumbnails(project, projectUpdateDTO.getThumbnailDTOS());

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
                        .name(participantDTO.getName())
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
                            .name(participantDTO.getName())
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
                            .name(participantDTO.getName())
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
            link.updateLink(project, linkDto.getUrl(), linkDto.getDescription());
            links.add(link);
        }
        linkRepository.saveAll(links);
    }

    protected void updateThumbnails(ProjectEntity project, List<ThumbnailDTO> thumbnailDTOs) {
        // 이전에 저장된 모든 섬네일 삭제
        thumbnailRepository.deleteByProjectId(project.getId());
        thumbnailRepository.flush();
        // 새로운 섬네일 생성 및 저장
        List<ThumbnailEntity> thumbnails = new ArrayList<>();
        for (ThumbnailDTO thumbnailDto : thumbnailDTOs) {
            ThumbnailEntity thumbnail = new ThumbnailEntity();
            thumbnail.updateThumbnail(project, thumbnailDto.getUrl());
            thumbnails.add(thumbnail);
        }
        thumbnailRepository.saveAll(thumbnails);
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
                .project_id(projectEntity.getId())
                .title(projectEntity.getTitle())
                .description(projectEntity.getDescription())
                .devStatus(projectEntity.getDevStatus())
                .devType(projectEntity.getDevType())
                .thumbnails(projectEntity.getThumbnails())
                .build();
    }

    public static ProjectDetailDTO entity2ProjectDetailDTO(ProjectEntity projectEntity){
        return ProjectDetailDTO.builder()
                .project_id(projectEntity.getId())
                .title(projectEntity.getTitle())
                .description(projectEntity.getDescription())
                .start(projectEntity.getStart())
                .end(projectEntity.getEnd())
                .thumbnails(projectEntity.getThumbnails())
                .devStatus(projectEntity.getDevStatus())
                .devType(projectEntity.getDevType())
                .participants(projectEntity.getParticipants())
                .outsiderParticipants(projectEntity.getOutsiderParticipants())
                .links(projectEntity.getLinks())
                .build();
    }


    public ProjectEntity projectCreateDTO2ProjectEntity(ProjectCreateDTO projectCreateDTO){

        return ProjectEntity.builder()
                .title(projectCreateDTO.getTitle())
                .description(projectCreateDTO.getDescription())
                .start(projectCreateDTO.getStart())
                .end(projectCreateDTO.getEnd())
                .devStatus(projectCreateDTO.getDevStatus())
                .devType(projectCreateDTO.getDevType())
                .build();
    }

}
