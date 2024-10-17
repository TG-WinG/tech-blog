package kr.tgwing.tech.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.tgwing.tech.blog.dto.CommentForm;
import kr.tgwing.tech.blog.dto.CommentView;
import kr.tgwing.tech.blog.dto.LikeHistoryView;
import kr.tgwing.tech.blog.dto.PostDetail;
import kr.tgwing.tech.blog.dto.PostForm;
import kr.tgwing.tech.blog.dto.PostOverview;
import kr.tgwing.tech.blog.dto.PostQuery;
import kr.tgwing.tech.blog.dto.ReplyForm;
import kr.tgwing.tech.blog.dto.ReplyView;

/**
 * {@summary 블로그 포스트 및 댓글의 CRUD를 담당하는 서비스.}
 *
 * @author 3un0ia, wwingyu
 */
public interface PostService {

    public PostDetail getPost(Long postId);
    public PostOverview getPostOverview(Long postId);
    public PostDetail createPost(PostForm form, String writerStudentNumber);
    public PostDetail updatePost(Long postId, PostForm form, String writerStudentNumber);
    public void deletePost(Long postId, String writerStudentNumber);
    public Page<PostOverview> getPostOverviews(PostQuery query, Pageable pageable);

    public CommentView createComment(Long postId, CommentForm form, String writerStudentNumber);
    public CommentView updateComment(Long postId, Long commentId, CommentForm form, String writerStudentNumber);
    public void deleteComment(Long postId, Long commentId, String writerStudentNumber);
    public Page<CommentView> getComments(Long postId, Pageable pageable);

    public ReplyView createReply(Long postId, Long commentId, ReplyForm form, String writerStudentNumber);
    public ReplyView updateReply(Long postId, Long commentId, Long replyId, ReplyForm form, String writerStudentNumber);
    public void deleteReply(Long postId, Long commentId, Long replyId, String writerStudentNumber);
    public Page<ReplyView> getReplies(Long postId, Long commentId, Pageable pageable);

    public LikeHistoryView toggleLike(Long postId, String userStudentNumber);
}
