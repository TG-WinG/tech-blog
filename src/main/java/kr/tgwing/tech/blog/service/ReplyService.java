package kr.tgwing.tech.blog.service;

import kr.tgwing.tech.blog.dto.ReplyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReplyService {

    List<ReplyDto> getAll(Long postId);
    ReplyDto post(ReplyDto reqDto, Long postId);
    ResponseEntity delete(Long postId, Long commentId, String token);
    Page<ReplyDto> findRepliesInPage(Pageable pageable, Long postId);
}
