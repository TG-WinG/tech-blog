package kr.tgwing.tech.reply.controller;

import jakarta.transaction.Transactional;
import kr.tgwing.tech.reply.dto.ReplyCreationDto;
import kr.tgwing.tech.reply.dto.ReplyDto;
import kr.tgwing.tech.reply.entity.ReplyEntity;
import kr.tgwing.tech.reply.service.ReplyService;
import kr.tgwing.tech.reply.service.ReplyServiceImpl;
import kr.tgwing.tech.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReplyController {

    /*
     * 특정 게시물 댓글 전부 가져오기 - GET, /tgwing.kr/info/notice/comment/{id}
     * 게시물 댓글 달기 - POST, /tgwing.kr/notice/comment/post/{id}
     * 자신이 작성한 댓글 삭제 - DELETE, /tgwing.kr/notice/comment/delete/{id}
     * */

    private final ReplyServiceImpl replyService;

    @PostMapping("/notice/comment/{postId}")
    public ResponseEntity<ReplyDto> postReply(@PathVariable Long postId,
                                              @RequestBody ReplyCreationDto reqDto,
                                              Principal principal) {
        log.info("--- Post Reply ---");

        String utilStudentId = principal.getName();

        ReplyDto result = replyService.post(reqDto, postId, utilStudentId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/notice/comment/{postId}")
    public ResponseEntity deleteReply(@PathVariable Long postId,
                                      @RequestBody ReplyDto reqDto,
                                      Principal principal) {
        log.info("--- Delete Own Reply ---");

        String utilStudentId = principal.getName();
        replyService.delete(postId, reqDto, utilStudentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/notice/comment/{postId}")
    @Transactional
    public ResponseEntity<Page> getReplyInPage(@PathVariable Long postId,
                                               @RequestParam(required = false) int size,
                                               @PageableDefault(size = 15) Pageable pageable) {
        log.info("-- Get Replies in Page --");
        // 첫 번째 페이지 page = 0이므로, page-1로 전달 -> 1부터 요청할 수 있도록
        Page<ReplyDto> repliesInPage = replyService.findRepliesInPage(size, pageable, postId);

        return ResponseEntity.ok(repliesInPage);
    }
}
