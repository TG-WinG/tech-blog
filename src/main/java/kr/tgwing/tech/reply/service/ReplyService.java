package kr.tgwing.tech.reply.service;

import kr.tgwing.tech.reply.dto.ReplyCreationDto;
import kr.tgwing.tech.reply.dto.ReplyDto;
import kr.tgwing.tech.reply.entity.ReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReplyService {

    List<ReplyDto> getAll(Long postId);
    ReplyDto post(ReplyCreationDto reqDto, Long postId, String utilStudentId);
    void delete(Long postId, ReplyDto reqDto, String token);
    Page<ReplyDto> findRepliesInPage(int size, Pageable pageable, Long postId);
}
