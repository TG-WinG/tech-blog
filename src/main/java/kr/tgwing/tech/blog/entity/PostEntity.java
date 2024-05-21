package kr.tgwing.tech.blog.entity;


import jakarta.persistence.*;
import kr.tgwing.tech.blog.dto.PostDto;
import kr.tgwing.tech.common.BaseEntity;
import lombok.*;

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

    public void updateContent(PostDto postDto) {
        this.title = postDto.getTitle();
        this.content = postDto.getContent();
        this.thumbnail = postDto.getThumbnail();
    }

    public void setWriter(Long writer) {
        this.writer = writer;
    }
}
