package kr.tgwing.tech.reply.controller;

import jakarta.transaction.Transactional;
import kr.tgwing.tech.reply.dto.ReplyDto;
import kr.tgwing.tech.reply.service.ReplyService;
import kr.tgwing.tech.reply.service.ReplyServiceImpl;
import kr.tgwing.tech.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/info/notice/comment/{id}") // 특정 게시물 전체 댓글 가져오기
    public ResponseEntity<List<ReplyDto>> getAllReplies(@PathVariable("id") Long postId) {
        System.out.println("--- Get All Replies ---");

        // post id 받고, 해당 post의 id를 reference로 가진 replies를 가져옴
        List<ReplyDto> results = replyService.getAll(postId);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/notice/comment/post/{id}")
    public ResponseEntity<ReplyDto> postReply(@PathVariable("id") Long postId,
                                              @RequestBody ReplyDto reqDto) {
        System.out.println("--- Post Reply ---");

        ReplyDto result = replyService.post(reqDto, postId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/notice/comment/delete/{id}")
    public ResponseEntity deleteReply(@PathVariable("id") Long postId,
                                      @RequestBody ReplyDto reqDto,
                                      @RequestHeader("authorization") String token ) {
        System.out.println("--- Delete Own Reply ---");

        String jwt = token.split(" ")[1];
        String tokenStudentId = jwtUtil.getStudentId(jwt);

        return replyService.delete(postId, reqDto, tokenStudentId);
    }

    @GetMapping("/notice/comment/{id}")
    @Transactional
    public ResponseEntity<Page> getReplyInPage(@PathVariable("id") Long postId,
                                               @RequestParam int page,
                                               @RequestParam(required = false) int size,
                                               @PageableDefault(size = 15) Pageable pageable) {
        System.out.println("-- Get Replies in Page --");
        // 첫 번째 페이지 page = 0이므로, page-1로 전달 -> 1부터 요청할 수 있도록
        Page<ReplyDto> repliesInPage = replyService.findRepliesInPage(page-1, size, pageable, postId);

        return ResponseEntity.ok(repliesInPage);
    }
}
