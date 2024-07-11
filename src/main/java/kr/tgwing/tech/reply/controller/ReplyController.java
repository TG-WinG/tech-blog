package kr.tgwing.tech.reply.controller;

import kr.tgwing.tech.reply.dto.ReplyDto;
import kr.tgwing.tech.reply.service.ReplyServiceImpl;
import kr.tgwing.tech.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReplyController {

    /*
     * 특정 게시물 댓글 전부 가져오기 - GET, /tgwing.kr/info/notice/comment/{id}
     * 게시물 댓글 달기 - POST, /tgwing.kr/notice/comment/post/{id}
     * 자신이 작성한 댓글 삭제 - DELETE, /tgwing.kr/notice/comment/delete/{id}
     * */

    private final ReplyServiceImpl replyService;
    private final JwtUtil jwtUtil;

    @GetMapping("/blog/{postId}/comment") // 특정 게시물 전체 댓글 가져오기
    public ResponseEntity<Page<ReplyDto>> getReplyInPage(Pageable pageable, @PathVariable("postId") Long postId) {
        System.out.println("--- Get Replies in Page ---");

        Page<ReplyDto> repliesInPage = replyService.findRepliesInPage(pageable, postId);

        return ResponseEntity.ok(repliesInPage);
    }

    @PostMapping("/blog/{postId}/comment")
    public ResponseEntity<ReplyDto> postReplyInPage(@PathVariable("postId") Long postId,
                                              @RequestBody ReplyDto reqDto) {
        System.out.println("--- Post Reply ---");

        ReplyDto result = replyService.post(reqDto, postId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/blog/{postId}/comment/{commentId}")
    public ResponseEntity deleteReply(@PathVariable("postId") Long postId,
                                      @PathVariable("commentId") Long commentId,
                                      @RequestHeader("authorization") String token ) {
        System.out.println("--- Delete Own Reply ---");

        String jwt = token.split(" ")[1];
        String tokenStudentId = jwtUtil.getStudentId(jwt);

        return replyService.delete(postId, commentId, tokenStudentId);
    }

    @GetMapping("/blog/{postId}/comment/{commentId}")
    public ResponseEntity<ReplyDto> getReply(@PathVariable("postId") Long postId, 
                                             @PathVariable("commentId") Pageable pageable) {
        System.out.println("-- Get Replies in Page --");
        // 첫 번째 페이지 page = 0이므로, page-1로 전달 -> 1부터 요청할 수 있도록
        return ResponseEntity.internalServerError().build();
    }
}
