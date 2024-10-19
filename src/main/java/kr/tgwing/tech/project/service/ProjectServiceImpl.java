package kr.tgwing.tech.project.service;

import kr.tgwing.tech.project.domain.Image;
import kr.tgwing.tech.project.domain.Link;
import kr.tgwing.tech.project.domain.Participant;
import kr.tgwing.tech.project.domain.Project;
import kr.tgwing.tech.project.dto.*;
import kr.tgwing.tech.project.exception.ProjectNotFoundException;
import kr.tgwing.tech.project.repository.ImageRepository;
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
    private final ImageRepository imageRepository;

    /*
    * 페이지네이션 안함.
    * */
    @Transactional
    public List<ProjectBriefDTO> getProjects() {
        List<Project> projects = projectRepository.findAll();
        if(projects == null){ return null; }
        else { return projects.stream()
                    .map(ProjectServiceImpl::entity2ProjectBriefDTO)
                    .toList(); }
    }

    public Long createProjects(ProjectCreateDTO projectCreateDTO) {
        Project project = projectCreateDTO.toEntity(projectCreateDTO);
        project.getParticipants().stream().forEach(participant -> participant.setProject(project));
        project.getLinks().stream().forEach(link -> link.setProject(project));
        project.getImageUrls().stream().forEach(url -> url.setProject(project));
        Project saveProject = projectRepository.save(project);

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
//        findProject.updateProject(projectUpdateDTO);
//
//        findProject.setParticipants(projectUpdateDTO.getParticipants());
//        findProject.getParticipants().forEach(participant -> participant.setProject(findProject));
//        findProject.setLinks(projectUpdateDTO.getLinks());
//        findProject.getLinks().forEach(link -> link.setProject(findProject));
//        findProject.setImageUrls(projectUpdateDTO.getImageUrls());
//        findProject.getImageUrls().forEach(url -> url.setProject(findProject));

        List<Participant> findParticipants = participantRepository.findAllByProjectId(project_id);
        List<Link> findLinks = linkRepository.findAllByProjectId(project_id);
        List<Image> findImgUrls = imageRepository.findAllByProjectId(project_id);

        List<Participant> participants = projectUpdateDTO.getParticipants().stream()
                .map(ParticipantDTO::toParticipantEntity)
                .toList();
        updateParticipants(findProject, findParticipants, participants);
        List<Link> links = projectUpdateDTO.getLinks().stream()
                .map(LinkDTO::toLinkEntity)
                .toList();
        updateLinks(findProject, findLinks, links);
        List<Image> images = projectUpdateDTO.getImageUrls().stream()
                        .map( imgUrl -> Image.builder().imageUrl(imgUrl).build()).toList();
        updateImage(findProject, findImgUrls, images);

        findProject.updateProject(projectUpdateDTO);
        return findProject.getId();
    }

    public void deleteProject(Long project_id) {
        Project findProject = projectRepository.findById(project_id)
                .orElseThrow(ProjectNotFoundException::new);
        projectRepository.deleteById(project_id);
    }

    //--------------------------------------

    @Transactional
    public void updateLinks(Project project, List<Link> findLinks, List<Link> newLinks) {
        for (Link link : findLinks) {
            if (!newLinks.contains(link)) {
                linkRepository.delete(link);
            }
        }

        for (Link newLink : newLinks) {
            if (!findLinks.contains(newLink)) {
                newLink.setProject(project);
                linkRepository.save(newLink);
            }
        }
    }

    @Transactional
    public void updateParticipants(Project project, List<Participant> findParticipants, List<Participant> newParticipants) {
        for (Participant participant : findParticipants) {
            if (!newParticipants.contains(participant)) {
                participantRepository.delete(participant);
            }
        }

        for (Participant newParticipant : newParticipants) {
            if (!findParticipants.contains(newParticipant)) {
                newParticipant.setProject(project);
                participantRepository.save(newParticipant);
            }
        }
    }

    @Transactional
    public void updateImage(Project project, List<Image> findImgs, List<Image> newImgs) {
        for (Image findImg : findImgs) {
            if (!newImgs.contains(findImg)) {
                imageRepository.delete(findImg);
            }
        }

        for (Image newImg : newImgs) {
            if (!findImgs.contains(newImg)) {
                newImg.setProject(project);
                imageRepository.save(newImg);
            }
        }
    }

    public static ProjectBriefDTO entity2ProjectBriefDTO(Project project){
        return ProjectBriefDTO.builder()
                .id(project.getId())
                .title(project.getTitle())
                .start(project.getStartDate())
                .end(project.getEndDate())
                .description(project.getDescription())
                .thumbnail(project.getImageUrls().stream().findFirst().get().getImageUrl())
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
                .devStatus(project.getDevStatus())
                .devType(project.getDevType())
                .imageUrls(project.getImageUrls().stream()
                        .map(url -> Image.toDto(url)).toList())
                .participants(project.getParticipants().stream()
                        .map(Participant::toDTO).toList())
                .links(project.getLinks().stream()
                        .map(Link::toDTO).toList())
                .build();
    }


}
