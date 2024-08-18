package kr.tgwing.tech.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "hashtag")
public class HashTagEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    @Column(name = "hashtag_name")
    private String name;

//    @Column(name = "creator_id")
//    @JoinColumn(referencedColumnName = "student_id", table = "student")
//    private Long creatorId;

    @OneToMany(mappedBy = "hashtag")
    @Builder.Default
    private Set<PostTagEntity> postTags = new HashSet<>();
}
