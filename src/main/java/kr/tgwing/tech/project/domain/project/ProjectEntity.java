package kr.tgwing.tech.project.domain.project;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@DynamicInsert
@NoArgsConstructor
@Table(name= "project")
public class ProjectEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="project_id")
    private Long id;

    private String title;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private String thumbnail;
    private String devStatus;
    private String devType;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<ParticipantEntity> participants = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<LinkEntity> links = new ArrayList<>();

    @Builder
    public ProjectEntity(Long id, String title, String description, LocalDateTime start, LocalDateTime end, String thumbnail, String devStatus, String devType, List<ParticipantEntity> participants, List<LinkEntity> links) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.thumbnail = thumbnail;
        this.devStatus = devStatus;
        this.devType = devType;
        this.participants = participants;
        this.links = links;
    }
}
