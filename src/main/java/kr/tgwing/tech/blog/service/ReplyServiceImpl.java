package kr.tgwing.tech.blog.service;

import kr.tgwing.tech.blog.entity.PostEntity;
import kr.tgwing.tech.blog.exception.post.PostNotFoundException;
import kr.tgwing.tech.blog.exception.reply.ReplyBadRequestException;
import kr.tgwing.tech.blog.exception.reply.ReplyForbiddenException;
import kr.tgwing.tech.blog.exception.reply.ReplyNotFoundException;
import kr.tgwing.tech.blog.repository.PostRepository;
import kr.tgwing.tech.blog.dto.ReplyDto;
import kr.tgwing.tech.blog.entity.ReplyEntity;
import kr.tgwing.tech.blog.repository.ReplyRepository;

import kr.tgwing.tech.user.entity.User;
import kr.tgwing.tech.user.exception.UserNotFoundException;
import kr.tgwing.tech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.tgwing.tech.blog.entity.ReplyEntity.toDto;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;

    public List<ReplyDto> getAll(Long postId) {
//        Optional<PostEntity> postById = postRepository.findById(postId);
//        PostEntity post = postById.orElseThrow();

        List<ReplyEntity> replies = replyRepository.findAllByPost(postId);

        if (replies.isEmpty()) {
//            System.out.println("댓글 없음");
//            throw new ReplyNotFoundException();
        }
        else {
//            // 확인차 남기는 로그
//            for (ReplyEntity reply : replies) {
//                System.out.println("reply = " + reply);
//            }
            List<ReplyDto> dtos = replies.stream().map(replyEntity -> toDto(replyEntity)).collect(Collectors.toList());
            return dtos;
        }
        return null;
    }

    public ReplyDto post(ReplyDto reqDto, Long postId) {

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        ReplyEntity replyEntity = ReplyDto.toEntity(reqDto, postId);

        ReplyEntity savedEntity = replyRepository.save(replyEntity);
        ReplyDto dto = toDto(savedEntity);

        return dto;
    }

    public ResponseEntity delete(Long postId, Long commentId, String tokenStudentId) {

        // 요청으로 들어온 post의 id = postId

        // requestDto - postId, writer, description, time

        // tokenStudentId - 토큰 발급자 (현 로그인 사용자)


        /**
         * reqDto.getId() -> 레포지토리에서 엔티티 찾기 == ReplyEntity
         * replyEntity -> getPost() - 댓글이 의존하고 있는 게시글의 아이디 가졍기
         * replyEntity.getPost() != postId이면 잘못된 주소.
         *
         * token에서 꺼낸 studentId (학번)
         * reqDto.getwriter() -> 레포지토리에서 엔티티 찾기 == UserEntity
         * userEntity.studentId != token.studentId 이면 삭제 권한 없음
         */

        ReplyEntity replyEntity = replyRepository.findById(commentId)
                .orElseThrow(ReplyNotFoundException::new);

        User userEntity = userRepository.findById(replyEntity.getWriter())
                .orElseThrow(UserNotFoundException::new);
//        UserEntity userEntity = userById.orElseThrow(UserNotFoundException::new);


        if(userEntity.getStudentId().equals(tokenStudentId)) { // 작성자, 수정자 확인

            if(replyEntity.getPost().equals(postId)) {
                System.out.println("-----  Delete Reply  ------");
                replyRepository.delete(replyEntity);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else {
                System.out.println("요청된 게시글 ID와 삭제를 원하는 댓글이 속한 게시글 ID이 서로 다름");
                throw new ReplyBadRequestException();
            }
        }
        else {
            System.out.println("URL 요청자와 공지 작성자가 다름");
            throw new ReplyForbiddenException();
        }
    }

    public Page<ReplyDto> findRepliesInPage(Pageable pageable, Long postId) {
        Page<ReplyEntity> replyPage = replyRepository.findAllByPostOrderByModDateDesc(pageable, postId);

        List<ReplyEntity> replies = replyPage.getContent();

        List<ReplyDto> dtos = replies.stream().map(replyEntity -> toDto(replyEntity)).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, replyPage.getTotalElements());
    }
}
