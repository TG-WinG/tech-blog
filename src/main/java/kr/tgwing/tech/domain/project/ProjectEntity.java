package kr.tgwing.tech.domain.project;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Builder
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "projects")
public class ProjectEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="project_id")
    private Long id;

    private String project_name;
}
