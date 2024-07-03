package kr.tgwing.tech.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.tgwing.tech.common.BaseEntity;
import kr.tgwing.tech.project.domain.Enum.DevType;
import kr.tgwing.tech.project.dto.ProjectUpdateDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor
@Table(name= "project")
@Validated
@Setter
public class ProjectEntity extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="project_id")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ThumbnailEntity> thumbnails = new ArrayList<>();

    @NotNull
    private String devStatus;


    @Enumerated(EnumType.STRING)
    private DevType devType;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ParticipantEntity> participants = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OutsiderParticipantEntity> outsiderParticipants = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LinkEntity> links = new ArrayList<>();

    @Builder
    public ProjectEntity(Long id, String title, String description, LocalDateTime start, LocalDateTime end, List<OutsiderParticipantEntity> outsiderParticipants,List<ThumbnailEntity> thumbnails, String devStatus, DevType devType, List<ParticipantEntity> participants, List<LinkEntity> links) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.thumbnails = thumbnails;
        this.devStatus = devStatus;
        this.devType = devType;
        this.participants = participants;
        this.outsiderParticipants = outsiderParticipants;
        this.links = links;
    }

    public void updateProjectByProjectUpdateDTO(ProjectUpdateDTO projectUpdateDTO) {
        if (projectUpdateDTO.getTitle() != null) {
            this.title = projectUpdateDTO.getTitle();
        }
        if (projectUpdateDTO.getDescription() != null) {
            this.description = projectUpdateDTO.getDescription();
        }
        if (projectUpdateDTO.getStart() != null) {
            this.start = projectUpdateDTO.getStart();
        }
        if (projectUpdateDTO.getEnd() != null) {
            this.end = projectUpdateDTO.getEnd();
        }
        if (projectUpdateDTO.getDevStatus() != null) {
            this.devStatus = projectUpdateDTO.getDevStatus();
        }
        if (projectUpdateDTO.getDevType() != null) {
            this.devType = projectUpdateDTO.getDevType();
        }
    }
}
