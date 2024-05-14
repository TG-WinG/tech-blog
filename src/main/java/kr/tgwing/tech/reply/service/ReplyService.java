package kr.tgwing.tech.reply.service;

import kr.tgwing.tech.reply.dto.ReplyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReplyService {

    List<ReplyDto> getAll(Long postId);
    ReplyDto post(ReplyDto reqDto, Long postId);
    ResponseEntity delete(Long postId, ReplyDto reqDto, String token);
    Page<ReplyDto> findRepliesInPage(int page, int size, Pageable pageable, Long postId);
}
