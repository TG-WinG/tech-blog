package kr.tgwing.tech.blog.service;


import kr.tgwing.tech.blog.dto.PostCreationDto;
import kr.tgwing.tech.blog.dto.PostDto;
import kr.tgwing.tech.blog.entity.HashTagEntity;
import kr.tgwing.tech.blog.entity.PostEntity;
import kr.tgwing.tech.blog.entity.PostTagEntity;
import kr.tgwing.tech.blog.exception.post.PostNotFoundException;
import kr.tgwing.tech.blog.exception.post.UserIsNotPostWriterException;
import kr.tgwing.tech.blog.exception.post.WrongPostRequestException;
import kr.tgwing.tech.blog.repository.HashtagRepository;
import kr.tgwing.tech.blog.repository.PostRepository;
import kr.tgwing.tech.blog.repository.PostTagRepository;
import kr.tgwing.tech.user.entity.User;
import kr.tgwing.tech.user.exception.UserNotFoundException;
import kr.tgwing.tech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static kr.tgwing.tech.blog.entity.PostEntity.toDto;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    private HashtagRepository hashtagRepository;
    @Autowired
    private PostTagRepository postTagRepository;

    @Override
    public PostDto getPost(Long postId) // 특정 게시글 가져오기
    {
        Set<HashTagEntity> hashtags = new HashSet<>();
        // 입력된 postId에 해당하는 글 찾기
        Optional<PostEntity> postEntityInOp = postRepository.findById(postId);
        PostEntity postEntity = postEntityInOp.orElseThrow(PostNotFoundException::new);

        List<PostTagEntity> postTags = postTagRepository.findAllByPostId(postId);
        postTags.forEach(posttag -> {
            Long tagId = posttag.getHashtagId();
            HashTagEntity tagEntity = hashtagRepository.findById(tagId).orElseThrow();
            hashtags.add(tagEntity);
        });
        System.out.println(postEntity.toString());

        return toDto(postEntity, hashtags);
    }



    private Page<PostEntity> searchPosts(String search, PageRequest pageRequest) { // 공지 내용으로 검색하기
        return postRepository.findByTitleContains(search, pageRequest);
    }
    private Page<PostEntity> getAllPosts(PageRequest pageRequest) { // 모든 공지 가져오기
        return postRepository.findAllByOrderByIdDesc(pageRequest);
    }

    @Override
    public PostDto createPost(PostCreationDto requestDto, String utilStudentId)  // 공지 생성하기
    {
        // 글을 작성할 user 조회
        Optional<User> byStudentId = userRepository.findByStudentId(utilStudentId);
        User userEntity = byStudentId.orElseThrow(UserNotFoundException::new);

        // 현재 사용자와 요청 시 들어온 writer가 같은지 확인
        if (!Objects.equals(userEntity.getStudentId(), utilStudentId)) {
            throw new WrongPostRequestException();
        }

        // entity에 작성자 Id 설정해서 저장하기
        // 해시태그 엔티티로 바꿔와서 함께 블로그 엔티티 생성

        //getHashtag - 요청으로 들어온 해시태그 집합을 데이터베이스에 블로그 연관 테이블, 해시태그 테이블에 저장 및 엔티티 집합 반환
        Set<HashTagEntity> hashtags = getHashtag(requestDto.getHashtags());
        PostEntity postEntity = PostCreationDto.toEntity(requestDto);
        postEntity.setWriter(userEntity.getId());
        PostEntity savedEntity = postRepository.save(postEntity);

        hashtags.forEach(tag -> {
            PostTagEntity postTag = PostTagEntity.builder().postId(savedEntity.getId()).hashtagId(tag.getId()).build();
            postTagRepository.save(postTag);
        });

        System.out.println(toDto(savedEntity, hashtags).toString());

        return toDto(savedEntity, hashtags);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long postId, String utilStudentId) // 게시글 수정하기
    {
        Optional<PostEntity> postById = postRepository.findById(postId);
        PostEntity postEntity = postById.orElseThrow(PostNotFoundException::new);

        // 해당 URL을 요청자 ==  공지 작성자 일 때에만 수정 가능
        // 수정 목록 : title, content, thumbnail

        Optional<User> userById = userRepository.findById(postEntity.getWriter());
        User userEntity = userById.orElseThrow(UserNotFoundException::new); // 유저 notfound 예외처리

        log.info("util학번 = {}", utilStudentId);
        log.info("user학번 = {}", userEntity.getStudentId());

        // DB 내 게시글 작성자 - 요청된 유저 ID 동일 & DB 내 게시글 작성자 - 요청된 게시글 작성자 동일
        if(Objects.equals(userEntity.getStudentId(), utilStudentId)) {
            log.info("학번 일치 - 작성자 확인");
            Set<HashTagEntity> hashtags = getHashtag(postDto.getHashtags());
            postTagRepository.deleteAllByPostId(postId);

            postEntity.updateContent(postDto, hashtags); // entity 정보 수정

            PostEntity savedEntity = postRepository.save(postEntity);

            hashtags.forEach(tag -> {
                PostTagEntity postTag = PostTagEntity.builder().postId(savedEntity.getId()).hashtagId(tag.getId()).build();
                postTagRepository.save(postTag);
            });
            return toDto(savedEntity, hashtags);
        }
        else { // 공지 작성자가 아닌 경우 -- 수정 불가능
            log.info("학번 불일치 - 작성자가 아니므로 수정 불가능");
            throw new UserIsNotPostWriterException();
        }
    }
    @Override
    public void deletePost(Long postId, String utilStudentId) // 게시글 삭제하기
    {
        Optional<PostEntity> postById = postRepository.findById(postId);
        PostEntity postEntity = postById.orElseThrow(PostNotFoundException::new);

        Optional<User> userById = userRepository.findById(postEntity.getWriter());
        User userEntity = userById.orElseThrow(UserNotFoundException::new);

        // 해당 URL을 요청한 사람이 공지 작성자인 경우에만 삭제 가능
        if(Objects.equals(userEntity.getStudentId(), utilStudentId)) {
            log.info("학번 일치 - 작성자 확인");
            postTagRepository.deleteAllByPostId(postId);
            postRepository.deleteById(postId);
        } else {
            log.info("학번 불일치 - 작성자가 아니므로 삭제 불가능");
            throw new UserIsNotPostWriterException(); }
    }

    @Override
    public Page<PostDto> getPostsInPage(String text, Pageable pageable) {
        int size = pageable.getPageSize();
        int page = pageable.getPageNumber();
        long total = 0L;
        Page<PostEntity> postPage = null;

                // Post DB에서 Page 단위로 가져오기
        PageRequest pageRequest = PageRequest.of(page, size);

        // 파라미터 없이 get 요청된 경우 - 그냥 전체 반환
        if(text == null) {
            postPage = getAllPosts(pageRequest);
            total = postRepository.count();
            log.info("total = " + total);
        }
        else { // - 파라미터로 검색된 블로그만 걸러서 반환
            postPage = searchPosts(text, pageRequest);
            total = postRepository.countByTitleContains(text);
            log.info("total = " + total);
        }

        if(!(total > page * size)) {throw new WrongPostRequestException(); }
        // 이후 ExceptionHandler 정의 필요

        if (postPage.isEmpty()) {
            log.info("page 조회 불가");
        }

        // stream이 비어있는 경우
        postPage.stream().findFirst().orElseThrow(()-> new PostNotFoundException());

        // page가 총 데이터 건수를 초과하는 경우
        long totalCount = postPage.getTotalElements();
        long requestCount = (postPage.getTotalPages() - 1) * postPage.getSize();
        if(!(totalCount > requestCount)) { throw new WrongPostRequestException(); }

        // Entity -> Dto 변환 - Dto 담은 page 반환
        List<PostEntity> posts = postPage.getContent();
        List<PostDto> dtos = posts.stream().map(postEntity -> toDto(postEntity, null)).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, postPage.getTotalElements());
    }

    // 해시태그 이름 set 가져와서
    // 새로운 해시태그 저장 및 해시태그 엔티티 생성
    // 해시태그 엔티티 set 반환
    private Set<HashTagEntity> getHashtag(Set<String> hashtags) {
        Set<HashTagEntity> resultTagIds = new HashSet<>();
        for (String tagname : hashtags) {
            HashTagEntity hashTagEntity = hashtagRepository.findByName(tagname).orElseGet(() -> {
                HashTagEntity newHashtag = HashTagEntity.builder()
                        .name(tagname).build();
                return hashtagRepository.save(newHashtag);
            });
            resultTagIds.add(hashTagEntity);
        }

        return resultTagIds;
    }
}
