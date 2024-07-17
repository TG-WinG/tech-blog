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
    private int replyCount;

    @OneToMany(mappedBy = "post")
    @Builder.Default
    private Set<PostTagEntity> postTags = new HashSet<>();


//    @ManyToMany(cascade = CascadeType.PERSIST)
//    // 다대다 연관매핑 조인테이블 정의  name(조인테이블 이름)
//    // join/inverseJoin Column(현재 엔티티/반대엔티티의 키 컬럼)
//    // 각 name은 조인테이블에서 정의되는 이름
//    @JoinTable(
//            name = "post_hashtag",
//            joinColumns = @JoinColumn(name = "postId"),
//            inverseJoinColumns = @JoinColumn(name = "hashtagId")
//    )
//    Set<HashTagEntity> hashtags = new HashSet<>();


//    @OneToMany
//    @JoinColumn(name = "post")
//    private List<ReplyEntity> replies;

    public static PostDto toDto(PostEntity postEntity, Set<HashTagEntity> hashtags) {
        Set<String> tags = new HashSet<>();

        if (hashtags != null) {
            for (HashTagEntity hashtag : hashtags) {
                System.out.println(hashtag.getName());
                tags.add(hashtag.getName());
            }
        }

        return PostDto.builder()
                .id(postEntity.id)
                .writer(postEntity.writer)
                .title(postEntity.title)
                .content(postEntity.content)
                .thumbnail(postEntity.thumbnail)
                .hashtags(tags).build();
    }

    public void updateContent(PostDto postDto, Set<HashTagEntity> hashtags) {
        this.title = postDto.getTitle();
        this.content = postDto.getContent();
        this.thumbnail = postDto.getThumbnail();
    }

    public void setWriter(Long writer) {
        this.writer = writer;
    }
}
