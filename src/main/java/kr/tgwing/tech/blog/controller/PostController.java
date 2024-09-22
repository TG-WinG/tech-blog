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
        @PageableDefault Pageable pageable
    ) {
        return postService.getPostOverviews(query, pageable);
    }

    @GetMapping("{postId}") // 특정 블로그 가져오기 - GET, /api/blog/{postId}
    public PostDetail getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }

    @PostMapping // 블로그 작성 - POST, /api/blog
    public PostDetail createPost(
        @RequestBody PostForm form,
        Principal principal
    ) {
        String studentId = principal.getName();
        return postService.createPost(form, studentId);
    }

    //    @CrossOrigin
    @PutMapping("{postId}") // 블로그 수정 - PUT, /api/blog/{postId}
    public PostDetail updatePost(
        @PathVariable Long postId,
        @RequestBody PostForm form,
        Principal principal
    ) {
        String studentId = principal.getName();
        return postService.updatePost(postId, form, studentId);
    }

    @DeleteMapping("{postId}") // 블로그 삭제 - DELETE, /api/blog/{postid}
    public void delete(
        @PathVariable Long postId,
        Principal principal
    ) {
        String studentId = principal.getName();
        postService.deletePost(postId, studentId);
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
        String studentId = principal.getName();
        return postService.createComment(postId, form, studentId);
    }

    @PutMapping("{postId}/comment/{commentId}")
    public CommentView updateComment(
        @PathVariable Long postId,
        @PathVariable Long commentId,
        @RequestBody CommentForm form,
        Principal principal
    ) {
        String studentId = principal.getName();
        return postService.updateComment(postId, commentId, form, studentId);
    }

    @DeleteMapping("{postId}/comment/{commentId}")
    public void deletePost(
        @PathVariable Long postId,
        @PathVariable Long commentId,
        Principal principal
    ) {
        String studentId = principal.getName();
        postService.deleteComment(postId, commentId, studentId);
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
        String studentId = principal.getName();
        return postService.createReply(postId, commentId, form, studentId);
    }

    @PutMapping("{postId}/comment/{commentId}/reply/{replyId}")
    public ReplyView updateReply(
        @PathVariable Long postId,
        @PathVariable Long commentId,
        @PathVariable Long replyId,
        @RequestBody ReplyForm form,
        Principal principal
    ) {
        String studentId = principal.getName();
        return postService.updateReply(postId, commentId, replyId, form, studentId);
    }

    @DeleteMapping("{postId}/comment/{commentId}/reply/{replyId}")
    public void deletePost(
        @PathVariable Long postId,
        @PathVariable Long commentId,
        @PathVariable Long replyId,
        Principal principal
    ) {
        String studentId = principal.getName();
        postService.deleteReply(postId, commentId, replyId, studentId);
    }
}

