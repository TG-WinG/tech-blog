package kr.tgwing.tech.blog.service;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.tgwing.tech.blog.dto.CommentForm;
import kr.tgwing.tech.blog.dto.CommentView;
import kr.tgwing.tech.blog.dto.LikeHistoryView;
import kr.tgwing.tech.blog.dto.PostDetail;
import kr.tgwing.tech.blog.dto.PostForm;
import kr.tgwing.tech.blog.dto.PostOverview;
import kr.tgwing.tech.blog.dto.PostQuery;
import kr.tgwing.tech.blog.dto.ReplyForm;
import kr.tgwing.tech.blog.dto.ReplyView;
import kr.tgwing.tech.blog.entity.Comment;
import kr.tgwing.tech.blog.entity.Hashtag;
import kr.tgwing.tech.blog.entity.LikeHistory;
import kr.tgwing.tech.blog.entity.Post;
import kr.tgwing.tech.blog.entity.PostSpecifications;
import kr.tgwing.tech.blog.entity.Reply;
import kr.tgwing.tech.blog.exception.comment.CommentNotFoundException;
import kr.tgwing.tech.blog.exception.post.PostNotFoundException;
import kr.tgwing.tech.blog.exception.post.UserIsNotPostWriterException;
import kr.tgwing.tech.blog.exception.reply.ReplyNotFoundException;
import kr.tgwing.tech.blog.repository.CommentRepository;
import kr.tgwing.tech.blog.repository.LikeHistoryRepository;
import kr.tgwing.tech.blog.repository.PostRepository;
import kr.tgwing.tech.blog.repository.ReplyRepository;
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
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final LikeHistoryRepository likeHistoryRepository;

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
                .thumbnail(form.getThumbnail())
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
        return posts.map(PostOverview::of);
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
        post.increaseCommentCount();

        postRepository.save(post);
        commentRepository.save(newComment);

        return CommentView.of(newComment);
    }

    @Override
    public CommentView updateComment(Long postId, Long commentId, CommentForm form, String writerStudentNumber) {
        User writer = userRepository.findByStudentNumber(writerStudentNumber)
                .orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!comment.getWriter().equals(writer)) throw new RuntimeException();
        comment.setContent(form.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return CommentView.of(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId, String writerStudentNumber) {
        User writer = userRepository.findByStudentNumber(writerStudentNumber)
                .orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!comment.getWriter().equals(writer)) throw new RuntimeException();
        commentRepository.delete(comment);
        post.decreaseCommentCount();
        postRepository.save(post);
    }

    @Override
    public Page<CommentView> getComments(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        Page<Comment> comments = commentRepository.findAllByPost(post, pageable);
        return comments.map(CommentView::of);
    }

    @Override
    public ReplyView createReply(Long postId, Long commentId, ReplyForm form, String writerStudentNumber) {
        User writer = userRepository.findByStudentNumber(writerStudentNumber)
                .orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        Reply newReply = Reply.builder()
                .post(post)
                .comment(comment)
                .content(form.getContent())
                .writer(writer)
                .build();
        post.increaseCommentCount();

        replyRepository.save(newReply);

        return ReplyView.of(newReply);
    }

    @Override
    public ReplyView updateReply(Long postId, Long commentId, Long replyId, ReplyForm form, String writerStudentNumber) {
        User writer = userRepository.findByStudentNumber(writerStudentNumber)
                .orElseThrow(UserNotFoundException::new);

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(ReplyNotFoundException::new);

        if (!reply.getWriter().equals(writer)) throw new RuntimeException();

        reply.setContent(form.getContent());
        Reply updatedReply = replyRepository.save(reply);

        return ReplyView.of(updatedReply);
    }

    @Override
    public void deleteReply(Long postId, Long commentId, Long replyId, String writerStudentNumber) {
        User writer = userRepository.findByStudentNumber(writerStudentNumber)
                .orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(ReplyNotFoundException::new);

        if (!reply.getWriter().equals(writer)) throw new RuntimeException();
        replyRepository.delete(reply);
        post.decreaseCommentCount();
        postRepository.save(post);
    }

    @Override
    public Page<ReplyView> getReplies(Long postId, Long commentId, Pageable pageable) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        Page<Reply> replies = replyRepository.findAllByComment(comment, pageable);
        return replies.map(ReplyView::of);
    }

    @Override
    public LikeHistoryView toggleLike(Long postId, String userStudentNumber) {
        User user = userRepository.findByStudentNumber(userStudentNumber)
                .orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        LikeHistory.Key key = new LikeHistory.Key(user.getStudentId(), postId);
        LikeHistory likeHistory = likeHistoryRepository.findById(key).orElse(null);

        if (likeHistory == null) {
            likeHistory = LikeHistory.builder()
                        .user(user)
                        .post(post)
                        .canceled(false)
                        .build();
            post.increaseLikeCount();
        } else {
            likeHistory.toggle();
            if (likeHistory.isCanceled())
                post.decreaseLikeCount();
            else
                post.increaseLikeCount();
        }
        likeHistoryRepository.save(likeHistory);
        postRepository.save(post);

        return LikeHistoryView.of(likeHistory);
    }

}
