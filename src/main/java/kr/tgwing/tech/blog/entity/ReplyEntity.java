package kr.tgwing.tech.blog.entity;

import jakarta.persistence.*;
import kr.tgwing.tech.common.BaseEntity;
import kr.tgwing.tech.blog.dto.ReplyDto;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reply")
public class ReplyEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(referencedColumnName = "id", table = "user", nullable = false)
    private Long writer;

    @Column(nullable = false)
    private String description;

    @JoinColumn(referencedColumnName = "id", table = "post", nullable = false)
    private Long post;

//    @ManyToOne
//    @JoinColumn(name = "post")
//    private PostEntity post;

    public static ReplyDto toDto(ReplyEntity replyEntity) {
        return ReplyDto.builder()
                .id(replyEntity.id)
                .writer(replyEntity.writer)
                .description(replyEntity.description)
                .modDate(replyEntity.getModDate())
                .build();
    }

}
