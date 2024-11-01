package kr.tgwing.tech.project.domain;

import jakarta.persistence.*;
import kr.tgwing.tech.common.BaseEntity;
import kr.tgwing.tech.project.dto.LinkDTO;
import kr.tgwing.tech.project.dto.ParticipantDTO;
import kr.tgwing.tech.project.dto.ProjectUpdateDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor
@Table(name= "project")
public class Project extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="project_id")
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "dev_status")
    private String devStatus;

    @Column(name = "dev_type")
    private String devType;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> imageUrls = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Link> links = new ArrayList<>();

    @Builder
    public Project(Long id, String title, String description, LocalDate start, LocalDate end, List<Image> imageUrls, String devStatus, String devType, List<Participant> participants, List<Link> links) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = start;
        this.endDate = end;
        this.imageUrls = imageUrls;
        this.devStatus = devStatus;
        this.devType = devType;
        this.participants = participants;
        this.links = links;
    }

    public void setParticipants(List<ParticipantDTO> participants) {
        List<Participant> update = participants.stream()
                .map(ParticipantDTO::toParticipantEntity)
                .toList();
        this.participants = update;
    }

    public List<Participant> updateParticipants(List<ParticipantDTO> participants) {
        List<Participant> update = participants.stream()
                .map(ParticipantDTO::toParticipantEntity)
                .toList();
        return update;
    }

    public void setLinks(List<LinkDTO> links) {
        List<Link> update = links.stream()
                .map(LinkDTO::toLinkEntity)
                .toList();
        this.links = update;
    }

    public void setImageUrls(List<String> imageUrls) {
        List<Image> update = imageUrls.stream()
                .map( url -> Image.builder().imageUrl(url).build())
                .toList();
        this.imageUrls = update;
    }

    public void updateProject(ProjectUpdateDTO projectUpdateDTO) {
        this.title = projectUpdateDTO.getTitle();
        this.description = projectUpdateDTO.getDescription();
        this.startDate = projectUpdateDTO.getStart();
        this.endDate = projectUpdateDTO.getEnd();
        this.devStatus = projectUpdateDTO.getDevStatus();
        this.devType = projectUpdateDTO.getDevType();
//        this.participants = updateParticipants(projectUpdateDTO.getParticipants());
    }

}
