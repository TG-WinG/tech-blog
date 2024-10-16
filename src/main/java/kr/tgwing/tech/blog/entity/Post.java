package kr.tgwing.tech.blog.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import kr.tgwing.tech.common.BaseEntity;
import kr.tgwing.tech.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "post")
public class Post extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(name = "thumbnail_url")
    private String thumbnail;

    @Column(name = "comment_count")
    private int commentCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Hashtag> hashtags = new HashSet<>();


    public void setWriter(User writer) {
        this.writer = writer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setHashtags(Set<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    public void increaseCommentCount() { commentCount++; }

    public void decreaseCommentCount() { commentCount--; }

}
