package kr.tgwing.tech.blog.controller;

import kr.tgwing.tech.blog.dto.ReplyDto;
import kr.tgwing.tech.blog.service.ReplyServiceImpl;
import kr.tgwing.tech.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyServiceImpl replyService;
    private final JwtUtil jwtUtil;

    @GetMapping("/blog/{postId}/reply") // 특정 게시물 전체 댓글 가져오기
    public ResponseEntity<Page<ReplyDto>> getReplyInPage(Pageable pageable, @PathVariable("postId") Long postId) {
        System.out.println("--- Get Replies in Page ---");

        Page<ReplyDto> repliesInPage = replyService.findRepliesInPage(pageable, postId);

        return ResponseEntity.ok(repliesInPage);
    }

    @PostMapping("/blog/{postId}/reply")
    public ResponseEntity<ReplyDto> postReplyInPage(@PathVariable("postId") Long postId,
                                              @RequestBody ReplyDto reqDto) {
        System.out.println("--- Post Reply ---");

        ReplyDto result = replyService.post(reqDto, postId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/blog/{postId}/reply/{replyId}")
    public ResponseEntity deleteReply(@PathVariable("postId") Long postId,
                                      @PathVariable("replyId") Long replyId,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("--- Delete Own Reply ---");

        String studentId = userDetails.getUsername();

        return replyService.delete(postId, replyId, studentId);
    }

    @GetMapping("/blog/{postId}/reply/{replyId}")
    public ResponseEntity<ReplyDto> getReply(@PathVariable("postId") Long postId, 
                                             @PathVariable("replyId") Pageable pageable) {
        System.out.println("-- Get Replies in Page --");
        // 첫 번째 페이지 page = 0이므로, page-1로 전달 -> 1부터 요청할 수 있도록
        return ResponseEntity.internalServerError().build();
    }
}
