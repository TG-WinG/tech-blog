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
public class PostService {
    private final PostRepository postRepository;
//    private final UserRepository userRepository;
//    private final AuthService authService;

    public List<PostDto> getAllPosts() { // 모든 공지 가져오기
        List<PostEntity> allPosts = postRepository.findAll();

        // 가져온 List가 비어있는 경우 == 공지 없음
//        if (allPosts.isEmpty()) throw new PostNotFoundException();

        List<PostDto> dtos = allPosts.stream().map(postEntity -> toDto(postEntity)).collect(Collectors.toList());
        return dtos;
    }

    public PostDto getPost(Long postId) // 특정 게시글 가져오기
    {
        // 입력된 postId에 해당하는 글 찾기
        Optional<PostEntity> postEntityInOp = postRepository.findById(postId);
//        PostEntity postEntity = postEntityInOp.orElseThrow(PostNotFoundException::new); 예외처리 생략
        PostEntity postEntity = postEntityInOp.orElseThrow();

        return toDto(postEntity);
    }

//    @Transactional
//    public List<PostDto> getPostsByUserId(Long userId) // 특정 유저가 게시한 글 모두 불러오기
//    {
//        List<PostDto> postDtos = new ArrayList<>();
//
//        // 입력된 id에 해당하는 유저 찾기
//        Optional<UserEntity> userEntityInOp = userRepository.findById(userId);
//        UserEntity userEntity = userEntityInOp.orElseThrow();
//
//        // 해당 유저가 작성한 글 모두 가져오기
//        List<PostEntity> postEntities = userEntity.getPosts();
//
//        // Loop - 관리자 여부 확인해서 내보낼 글만 담기
//        for (PostEntity postEntity : postEntities) {
//            if (postEntity.getIsAdmin())
//                postDtos.add(toDto(postEntity));
//            else
//                continue;
//        }
//        // Loop 끝나면 내보내기
//        return postDtos;
//    }


    public List<PostDto> searchPosts(String search) // 공지 내용으로 검색하기
    {
        List<PostEntity> searchedEntity = postRepository.findByContentContains(search);
        // 가져온 List가 비어있는 경우 == 공지 없음
//        if (searchedEntity.isEmpty()) throw new PostNotFoundException();
        if (searchedEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NO_CONTENT);

        // 공지인지 확인 후 dto list에 담고, 공지가 아닌 경우 null로 변환
        List<PostDto> postDtos = searchedEntity.stream().map(postEntity -> toDto(postEntity)).collect(Collectors.toList());

        return postDtos;
    }

    public PostDto createPost(PostDto requestDto)  // 공지 생성하기
    {
        // 글을 작성할 user 조회
//        Optional<UserEntity> userById = userRepository.findById(requestDto.getWriter());
//        // userEntity를 받아오지 못한 경우 - 회원을 찾을 수 없음 (Exception)
//        UserEntity userEntity = userById.orElseThrow(UserNotFoundException::new);

//        if (userEntity.getLevel() == Level.MANAGER) { // user가 관리자인 경우
        if(true) { // level 해결까지는 무조건 if 통과
            PostEntity postEntity = toEntity(requestDto); // DTO기반 엔티티 생성: writer=요청 user

            PostEntity savedEntity = postRepository.save(postEntity);

            PostDto responseDto = toDto(savedEntity);
            return responseDto;

        } else { // user가 관리자가 아닌 경우
            // 공지 생성 불가능 - 접근 권한 없음 (Exception)
//            throw new HasNoAuthorityException();
            return null;
        }
    }

    public PostDto updatePost(PostDto postDto, Long userId, Long postId) // 게시글 수정하기
    {

        Optional<PostEntity> postById = postRepository.findById(postId);
//        PostEntity postEntity = postById.orElseThrow(PostNotFoundException::new);
        PostEntity postEntity = postById.orElseThrow();

        // 해당 URL을 요청자 ==  공지 작성자 일 때에만 수정 가능
        // 수정 목록 : title, content, thumbnail

        // DB 내 게시글 작성자 - 요청된 유저 ID 동일 & DB 내 게시글 작성자 - 요청된 게시글 작성자 동일
        if(postEntity.getWriter() == userId && postEntity.getWriter() == postDto.getWriter()) {
            System.out.println("유저 정보 일치");
            postEntity.updateContent(postDto); // 수정 요

            PostEntity savedEntity = postRepository.save(postEntity);
            PostDto responseDto = toDto(savedEntity);

            return responseDto;
        }
        else { // 공지 작성자가 아닌 경우 -- 수정 불가능
            System.out.println("유저 정보 불일치 - 작성자가 아니므로 수정 불가능");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
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

    public Page<PostDto> findPostsInPage(int page, int size, Pageable pageable) {
        // Post DB에서 Page 단위로 가져오기
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<PostEntity> postPage = postRepository.findAllByOrderByIdDesc(pageRequest);
        int total = postRepository.getCount();
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
