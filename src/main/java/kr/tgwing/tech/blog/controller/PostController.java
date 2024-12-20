package kr.tgwing.tech.blog.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import kr.tgwing.tech.blog.service.PostService;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping // 블로그 전체 가져오기 - GET, /api/blog
    public Page<PostOverview> getAllPostswithSearch(
        @ModelAttribute PostQuery query,
        @PageableDefault Pageable pageable,
        Principal principal
    ) {
        String studentNumber = getStudentNumber(principal);
        return postService.getPostOverviews(query, studentNumber, pageable);
    }

    @GetMapping("{postId}") // 특정 블로그 가져오기 - GET, /api/blog/{postId}
    public PostDetail getPost(
        @PathVariable(name = "postId") Long postId,
        Principal principal
    ) {
        String studentNumber = getStudentNumber(principal);
        return postService.getPost(postId, studentNumber);
    }

    @PostMapping // 블로그 작성 - POST, /api/blog
    public PostDetail createPost(
        @RequestBody PostForm form,
        Principal principal
    ) {
        String studentNumber = getStudentNumber(principal);
        return postService.createPost(form, studentNumber);
    }

    //    @CrossOrigin
    @PutMapping("{postId}") // 블로그 수정 - PUT, /api/blog/{postId}
    public PostDetail updatePost(
        @PathVariable Long postId,
        @RequestBody PostForm form,
        Principal principal
    ) {
        String studentNumber = getStudentNumber(principal);
        return postService.updatePost(postId, form, studentNumber);
    }

    @DeleteMapping("{postId}") // 블로그 삭제 - DELETE, /api/blog/{postid}
    public void delete(
        @PathVariable Long postId,
        Principal principal
    ) {
        String studentNumber = getStudentNumber(principal);
        postService.deletePost(postId, studentNumber);
    }

    @GetMapping("{postId}/comment")
    public Page<CommentView> getComments(
        @PathVariable Long postId,
        @PageableDefault Pageable pageable
    ) {
        return postService.getComments(postId, pageable);
    }

    @PostMapping("{postId}/comment")
    public CommentView createComment(
        @PathVariable Long postId,
        @RequestBody CommentForm form,
        Principal principal
    ) {
        String studentNumber = getStudentNumber(principal);
        return postService.createComment(postId, form, studentNumber);
    }

    @PutMapping("{postId}/comment/{commentId}")
    public CommentView updateComment(
        @PathVariable Long postId,
        @PathVariable Long commentId,
        @RequestBody CommentForm form,
        Principal principal
    ) {
        String studentNumber = getStudentNumber(principal);
        return postService.updateComment(postId, commentId, form, studentNumber);
    }

    @DeleteMapping("{postId}/comment/{commentId}")
    public void deletePost(
        @PathVariable Long postId,
        @PathVariable Long commentId,
        Principal principal
    ) {
        String studentNumber = getStudentNumber(principal);
        postService.deleteComment(postId, commentId, studentNumber);
    }

    @GetMapping("{postId}/comment/{commentId}/reply")
    public Page<ReplyView> getReplies(
        @PathVariable Long postId,
        @PathVariable Long commentId,
        @PageableDefault Pageable pageable
    ) {
        return postService.getReplies(postId, commentId, pageable);
    }

    @PostMapping("{postId}/comment/{commentId}/reply")
    public ReplyView createReply(
        @PathVariable Long postId,
        @PathVariable Long commentId,
        @RequestBody ReplyForm form,
        Principal principal
    ) {
        String studentNumber = getStudentNumber(principal);
        return postService.createReply(postId, commentId, form, studentNumber);
    }

    @PutMapping("{postId}/comment/{commentId}/reply/{replyId}")
    public ReplyView updateReply(
        @PathVariable Long postId,
        @PathVariable Long commentId,
        @PathVariable Long replyId,
        @RequestBody ReplyForm form,
        Principal principal
    ) {
        String studentNumber = getStudentNumber(principal);
        return postService.updateReply(postId, commentId, replyId, form, studentNumber);
    }

    @DeleteMapping("{postId}/comment/{commentId}/reply/{replyId}")
    public void deletePost(
        @PathVariable Long postId,
        @PathVariable Long commentId,
        @PathVariable Long replyId,
        Principal principal
    ) {
        String studentNumber = getStudentNumber(principal);
        postService.deleteReply(postId, commentId, replyId, studentNumber);
    }

    @PostMapping("{postId}/like")
    public LikeHistoryView toggleLike(
        @PathVariable Long postId,
        Principal principal
    ) {
        String studentNumber = getStudentNumber(principal);
        return postService.toggleLike(postId, studentNumber);
    }

    private String getStudentNumber(Principal principal) {
        String studentNumber = null;
        if (principal != null) {
            studentNumber = principal.getName();
        }
        return studentNumber;
    }
}

