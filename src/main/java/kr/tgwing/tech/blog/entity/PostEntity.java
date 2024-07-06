package kr.tgwing.tech.blog.entity;


import jakarta.persistence.*;
import kr.tgwing.tech.blog.dto.PostDto;
import kr.tgwing.tech.common.BaseEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="post")
public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JoinColumn(referencedColumnName = "id", table = "user")
    private Long writer;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    private String thumbnail;
//    @Column(name = "type", nullable = false)
//    private Type type;

    @ManyToMany(cascade = CascadeType.PERSIST)
    // 다대다 연관매핑 조인테이블 정의  name(조인테이블 이름)
    // join/inverseJoin Column(현재 엔티티/반대엔티티의 키 컬럼)
    // 각 name은 조인테이블에서 정의되는 이름
    @JoinTable(
            name = "post_hashtag",
            joinColumns = @JoinColumn(name = "postId"),
            inverseJoinColumns = @JoinColumn(name = "hashtagId")
    )
    private Set<HashTagEntity> hashtags = new HashSet<>();

    // 연관매핑: 다대일
    // 참조하는(외래키를 가진) 엔티티에서 사용
//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(referencedColumnName = "student_id")
//    private UserEntity user;

//    @OneToMany
//    @JoinColumn(name = "post")
//    private List<ReplyEntity> replies;

    public static PostDto toDto(PostEntity postEntity) {
        return PostDto.builder()
                .id(postEntity.id)
                .writer(postEntity.writer)
                .title(postEntity.title)
                .content(postEntity.content)
                .thumbnail(postEntity.thumbnail).build();
    }

    public void updateContent(PostDto postDto, Set<HashTagEntity> hashtags) {
        this.title = postDto.getTitle();
        this.content = postDto.getContent();
        this.thumbnail = postDto.getThumbnail();
        this.hashtags = hashtags;
    }

    public void setWriter(Long writer) {
        this.writer = writer;
    }
}
