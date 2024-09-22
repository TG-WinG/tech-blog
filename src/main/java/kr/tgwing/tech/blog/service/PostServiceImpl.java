package kr.tgwing.tech.blog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.tgwing.tech.blog.dto.CommentForm;
import kr.tgwing.tech.blog.dto.CommentView;
import kr.tgwing.tech.blog.dto.PostDetail;
import kr.tgwing.tech.blog.dto.PostForm;
import kr.tgwing.tech.blog.dto.PostOverview;
import kr.tgwing.tech.blog.dto.PostQuery;
import kr.tgwing.tech.blog.dto.ReplyForm;
import kr.tgwing.tech.blog.dto.ReplyView;
import kr.tgwing.tech.blog.entity.Comment;
import kr.tgwing.tech.blog.entity.Hashtag;
import kr.tgwing.tech.blog.entity.Post;
import kr.tgwing.tech.blog.entity.PostSpecifications;
import kr.tgwing.tech.blog.exception.post.PostNotFoundException;
import kr.tgwing.tech.blog.exception.post.UserIsNotPostWriterException;
import kr.tgwing.tech.blog.repository.PostRepository;
import kr.tgwing.tech.user.entity.User;
import kr.tgwing.tech.user.exception.UserNotFoundException;
import kr.tgwing.tech.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public PostDetail getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        return PostDetail.of(post);
    }

    @Override
    public PostDetail createPost(PostForm form, String writerStudentNumber) {
        // 글을 작성할 user 조회
        User writer = userRepository.findByStudentNumber(writerStudentNumber)
                .orElseThrow(UserNotFoundException::new);

        Post newPost = Post.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .writer(writer)
                .build();
        form.getHashtags().forEach(hashtag -> {
            newPost.getHashtags().add(Hashtag.builder()
                    .post(newPost)
                    .name(hashtag)
                    .build());
        });
        Post savedPost = postRepository.save(newPost);

        return PostDetail.of(savedPost);
    }

    @Override
    public PostDetail updatePost(Long postId, PostForm form, String writerStudentNumber) {
        Post post = postRepository.findById(postId)
               .orElseThrow(PostNotFoundException::new);

        User postWriter = userRepository.findById(post.getWriter().getStudentId())
               .orElseThrow(UserNotFoundException::new); // 유저 notfound 예외처리 

        if(post.getWriter().equals(postWriter)) {
            post.setTitle(form.getTitle());
            post.setContent(form.getContent());
            post.setThumbnail(form.getThumbnail());
            post.setHashtags(form.getHashtags().stream().map(hashtag -> {
                return Hashtag.builder()
                        .post(post)
                        .name(hashtag)
                        .build();
            }).collect(Collectors.toSet()));

            Post updatedPost = postRepository.save(post);

            return PostDetail.of(updatedPost);
        }
        else { // 공지 작성자가 아닌 경우 -- 수정 불가능
            log.info("학번 불일치 - 작성자가 아니므로 수정 불가능");
            throw new UserIsNotPostWriterException();
        }
    }

    @Override
    public void deletePost(Long postId, String writerStudentNumber) {
        Post post = postRepository.findById(postId)
               .orElseThrow(PostNotFoundException::new);

        User postWriter = userRepository.findByStudentNumber(writerStudentNumber)
                .orElseThrow(UserNotFoundException::new);

        // 해당 URL을 요청한 사람이 공지 작성자인 경우에만 삭제 가능
        if(post.getWriter().equals(postWriter)) {
            postRepository.deleteById(postId);
        } else {
            throw new UserIsNotPostWriterException(); }
    }

    @Override
    public Page<PostOverview> getPostOverviews(PostQuery query, Pageable pageable) {
        Specification<Post> spec = PostSpecifications.hasTitleLike(query.getKeyword())
                .or(PostSpecifications.hasContentLike(query.getKeyword()));

        if (query.getHashtag() != null && query.getHashtag().size() > 0) {
            spec = spec.and(PostSpecifications.hasHashtagIn(query.getHashtag()));
        }

        Page<Post> posts = postRepository.findAll(spec, pageable);
        List<PostOverview> overviews = posts.stream().map(PostOverview::of).collect(Collectors.toList());

        return new PageImpl<>(overviews, pageable, posts.getTotalElements());
    }

    @Override
    public PostOverview getPostOverview(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        return PostOverview.of(post);
    }

    @Override
    public CommentView createComment(Long postId, CommentForm form, String writerStudentNumber) {
        User writer = userRepository.findByStudentNumber(writerStudentNumber)
                .orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        Comment newComment = Comment.builder()
                .post(post)
                .content(form.getContent())
                .writer(writer)
                .build();
        post.getComments().add(newComment);

        postRepository.save(post);

        return CommentView.of(newComment);
    }

    @Override
    public CommentView updateComment(Long postId, Long commentId, CommentForm form, String writerStudentNumber) {
        User writer = userRepository.findByStudentNumber(writerStudentNumber)
                .orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        Comment updatedComment = null;
        for (Comment comment : post.getComments()) {
            if (comment.getId().equals(commentId)) {
                if (!comment.getWriter().equals(writer)) throw new RuntimeException();

                comment.setContent(form.getContent());
                updatedComment = comment;
                break;
            }
        }

        if (updatedComment == null) throw new RuntimeException();

        return CommentView.of(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId, String writerStudentNumber) {
        User writer = userRepository.findByStudentNumber(writerStudentNumber)
                .orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        Comment deletedComment = null;
        for (Comment comment : post.getComments()) {
            if (comment.getId().equals(commentId)) {
                if (!comment.getWriter().equals(writer)) throw new RuntimeException();

                deletedComment = comment;
                break;
            }
        }

        if (deletedComment == null) throw new RuntimeException();
        post.getComments().remove(deletedComment);
    }

    @Override
    public Page<CommentView> getComments(Long postId, Pageable pageable) {
        // TODO: Comment, Reply도 각각 Repository를 가지도록 리팩토링 후 수정해야 함. 
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        List<CommentView> commentViews = post.getComments().stream()
                .map(CommentView::of)
                .toList();
        return new PageImpl<>(commentViews, pageable, commentViews.size());
    }

    @Override
    public ReplyView createReply(Long postId, Long commentId, ReplyForm form, String writerStudentNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createReply'");
    }

    @Override
    public ReplyView updateReply(Long postId, Long commentId, Long replyId, ReplyForm form, String writerStudentNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateReply'");
    }

    @Override
    public void deleteReply(Long postId, Long commentId, Long replyId, String writerStudentNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteReply'");
    }

    @Override
    public Page<ReplyView> getReplies(Long postId, Long commentId, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReplies'");
    }

}
