package kr.tgwing.tech.blog.service;


import kr.tgwing.tech.blog.dto.PostDto;
import kr.tgwing.tech.blog.entity.PostEntity;
import kr.tgwing.tech.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.tgwing.tech.blog.dto.PostDto.toEntity;
import static kr.tgwing.tech.blog.entity.PostEntity.toDto;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
//    private final UserRepository userRepository;
//    private final AuthService authService;

    @Override
    public PostDto getPost(Long postId) // 특정 게시글 가져오기
    {
        // 입력된 postId에 해당하는 글 찾기
        Optional<PostEntity> postEntityInOp = postRepository.findById(postId);
//        PostEntity postEntity = postEntityInOp.orElseThrow(PostNotFoundException::new); 예외처리 생략
        PostEntity postEntity = postEntityInOp.orElseThrow();

        return toDto(postEntity);
    }



    private Page<PostEntity> searchPosts(String search, PageRequest pageRequest) { // 공지 내용으로 검색하기
        return postRepository.findByTitleContains(search, pageRequest);
    }
    private Page<PostEntity> getAllPosts(PageRequest pageRequest) { // 모든 공지 가져오기
        return postRepository.findAllByOrderByIdDesc(pageRequest);
    }

    @Override
    public PostDto createPost(PostDto requestDto, String token)  // 공지 생성하기
    {
        // 글을 작성할 user 조회
//        Optional<UserEntity> userById = userRepository.findById(requestDto.getWriter());
//        // userEntity를 받아오지 못한 경우 - 회원을 찾을 수 없음 (Exception)
//        UserEntity userEntity = userById.orElseThrow(UserNotFoundException::new);

        String jwt = token.split(" ")[1];
//        String studentId = authService.extractStudentId(jwt);

        // entity에는 저장된 썸네일의 uri만 추가로 넣어서 저장
        PostEntity postEntity = toEntity(requestDto);
        PostEntity savedEntity = postRepository.save(postEntity);

        return toDto(savedEntity);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long userId, Long postId, String token) // 게시글 수정하기
    {
        Optional<PostEntity> postById = postRepository.findById(postId);
//        PostEntity postEntity = postById.orElseThrow(PostNotFoundException::new);
        PostEntity postEntity = postById.orElseThrow();

        String jwt = token.split(" ")[1];

        // 해당 URL을 요청자 ==  공지 작성자 일 때에만 수정 가능
        // 수정 목록 : title, content, thumbnail

        // DB 내 게시글 작성자 - 요청된 유저 ID 동일 & DB 내 게시글 작성자 - 요청된 게시글 작성자 동일
        if(postEntity.getWriter() == userId && postEntity.getWriter() == postDto.getWriter()) {
            System.out.println("유저 정보 일치");
//            postEntity.updateContent(postDto); // 수정 요

            PostEntity savedEntity = postRepository.save(postEntity);
            return toDto(savedEntity);
        }
        else { // 공지 작성자가 아닌 경우 -- 수정 불가능
            System.out.println("유저 정보 불일치 - 작성자가 아니므로 수정 불가능");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
    @Override
    public void deletePost(Long userId, Long postId) // 게시글 삭제하기
    {
        Optional<PostEntity> postById = postRepository.findById(postId);
        PostEntity postEntity = postById.orElseThrow();

        // 해당 URL을 요청한 사람이 공지 작성자인 경우에만 삭제 가능
        if(postEntity.getWriter() == userId) {
            postRepository.deleteById(postId);
        } else {
            System.out.println("삭제 불가 - 작성자가 아님");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); }
    }

    @Override
    public Page<PostDto> getPostsInPage(String text, int page, Pageable pageable) {
        int size = pageable.getPageSize();
        long total = 0L;
        Page<PostEntity> postPage = null;

                // Post DB에서 Page 단위로 가져오기
        PageRequest pageRequest = PageRequest.of(page, size);

        // 파라미터 없이 get 요청된 경우 - 그냥 전체 반환
        if(text == null) {
            postPage = getAllPosts(pageRequest);
            total = postRepository.count();
            System.out.println("total = " + total);
        }
        else { // - 파라미터로 검색된 블로그만 걸러서 반환
            postPage = searchPosts(text, pageRequest);
            total = postRepository.countByTitleContains(text);
            System.out.println("total = " + total);
        }

        if(!(total > page * size)) {throw new ResponseStatusException(HttpStatus.BAD_REQUEST); }
        // 이후 ExceptionHandler 정의 필요

        if (postPage.isEmpty()) {
            System.out.println("조회 불가능");
        }

        // stream이 비어있는 경우
        postPage.stream().findFirst().orElseThrow(()-> new ResponseStatusException(HttpStatus.NO_CONTENT));

        // page가 총 데이터 건수를 초과하는 경우
        long totalCount = postPage.getTotalElements();
        long requestCount = (postPage.getTotalPages() - 1) * postPage.getSize();
        if(!(totalCount > requestCount)) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST); }

        // Entity -> Dto 변환 - Dto 담은 page 반환
        List<PostEntity> posts = postPage.getContent();
        List<PostDto> dtos = posts.stream().map(postEntity -> toDto(postEntity)).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, postPage.getTotalElements());
    }

}
