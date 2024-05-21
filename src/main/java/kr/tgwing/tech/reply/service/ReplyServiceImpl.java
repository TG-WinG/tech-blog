package kr.tgwing.tech.reply.service;

import kr.tgwing.tech.blog.entity.PostEntity;
import kr.tgwing.tech.blog.exception.PostNotFoundException;
import kr.tgwing.tech.blog.repository.PostRepository;
import kr.tgwing.tech.reply.dto.ReplyCreationDto;
import kr.tgwing.tech.reply.dto.ReplyDto;
import kr.tgwing.tech.reply.entity.ReplyEntity;
import kr.tgwing.tech.reply.exception.CantFindWriterException;
import kr.tgwing.tech.reply.exception.InappropriatePostReplyRelationException;
import kr.tgwing.tech.reply.exception.RepliesNotFoundException;
import kr.tgwing.tech.reply.exception.UserIsNotReplyWriterException;
import kr.tgwing.tech.reply.repository.ReplyRepository;
import kr.tgwing.tech.user.entity.UserEntity;
import kr.tgwing.tech.user.exception.UserNotFoundException;
import kr.tgwing.tech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.tgwing.tech.reply.entity.ReplyEntity.toDto;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;

    public List<ReplyDto> getAll(Long postId) {
        Optional<PostEntity> postById = postRepository.findById(postId);
        PostEntity post = postById.orElseThrow(PostNotFoundException::new);

        List<ReplyEntity> replies = replyRepository.findAllByPost(postId);

        if (replies.isEmpty()) {
            log.info("댓글 없음");
            throw new RepliesNotFoundException();
        }
        else {
//            // 확인차 남기는 로그
//            for (ReplyEntity reply : replies) {
//                System.out.println("reply = " + reply);
//            }
            List<ReplyDto> dtos = replies.stream().map(replyEntity -> toDto(replyEntity)).collect(Collectors.toList());
            return dtos;
        }
    }

    public ReplyDto post(ReplyCreationDto reqDto, Long postId, String utilStudentId) {

        Optional<UserEntity> byStudentId = userRepository.findByStudentId(utilStudentId);
        UserEntity userEntity = byStudentId.orElseThrow(UserNotFoundException::new);

        PostEntity post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        ReplyEntity replyEntity = ReplyCreationDto.toEntity(reqDto, post.getId());
        replyEntity.setWriter(userEntity.getId());

        ReplyEntity savedEntity = replyRepository.save(replyEntity);
        ReplyDto dto = toDto(savedEntity);

        return dto;
    }

    public void delete(Long postId, ReplyDto reqDto, String utilStudentId) {

        // 요청으로 들어온 post의 id = postId
        // requestDto - postId, writer, description, time
        // utilStudentId - 토큰 발급자 (현 로그인 사용자)

        Optional<ReplyEntity> replyById = replyRepository.findById(reqDto.getId()); // 삭제 요청된 댓글엔티티 찾기
        ReplyEntity replyEntity = replyById.orElseThrow(RepliesNotFoundException::new); // 없으면 그런 댓글 없음 throw

        Optional<UserEntity> userById = userRepository.findById(reqDto.getWriter()); // 삭제 요청된 댓글의 작성자 찾기
        UserEntity userEntity = userById.orElseThrow(CantFindWriterException::new); // 없으면 그런 작성자 없음 throw


        if(Objects.equals(userEntity.getStudentId(), utilStudentId)) { // 작성자, 수정자 일치 확인

            if(Objects.equals(replyEntity.getPost(), postId)) { // 요청된 댓글이 달린 블로그id, url로 요청된 블로그id 일치 확인
                log.info("-----  User id, Post id corresponding, Delete Reply  ------");
                replyRepository.delete(replyEntity);
            }
            else { // post id 불일치 - url이 잘못된 경우
                log.info("요청된 블로그의 ID와 삭제를 원하는 댓글이 속한 블로그 ID가 서로 다름");
                throw new InappropriatePostReplyRelationException();
            }
        }
        else {
            log.info("현재 유저는 댓글 작성자가 아님 - 삭제 불가");
            throw new UserIsNotReplyWriterException();
        }
    }

    public Page<ReplyDto> findRepliesInPage(int size, Pageable pageable, Long postId) {
        int page = pageable.getPageNumber();
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<ReplyEntity> replyPage = replyRepository.findAllByPostOrderByModDateDesc(pageRequest, postId);

        List<ReplyEntity> replies = replyPage.getContent();

        List<ReplyDto> dtos = replies.stream().map(replyEntity -> toDto(replyEntity)).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, replyPage.getTotalElements());
    }
}
