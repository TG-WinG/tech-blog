package kr.tgwing.tech.blog.controller;

import kr.tgwing.tech.blog.dto.PostCreationDto;
import kr.tgwing.tech.blog.dto.PostDto;
import kr.tgwing.tech.blog.exception.PathHasNoPostIdException;
import kr.tgwing.tech.blog.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostServiceImpl postService;

    @GetMapping("/blog") // 블로그 전체 가져오기 - GET, /api/blog
    public ResponseEntity<Page> getAllPostswithSearch(@RequestParam(value = "text", required = false) String text,
                                                      @PageableDefault Pageable pageable) {
        log.info("-- Retrieve All of Posts --");

        Page<PostDto> postsInPage = postService.getPostsInPage(text, pageable);

        return ResponseEntity.ok(postsInPage);
    }

    @GetMapping("/blog/{postId}") // 특정 블로그 가져오기 - GET, /api/blog/{postId}
    public ResponseEntity<PostDto> getPost(@PathVariable(required = false, name = "postId") Optional<Long> optional) {
        log.info("-- Retreive Specific Post by Id --");
        // 특정 게시글 id에 대한 post 정보를 모아 반환

        Long postId = optional.orElseThrow(PathHasNoPostIdException::new);

        PostDto post = postService.getPost(postId);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/blog") // 블로그 작성 - POST, /api/blog
    public ResponseEntity<PostDto> post(@RequestBody PostCreationDto requestDto,
                                        Principal principal) {
        log.info("-- Post new post --");
        log.info("Request Dto = " + requestDto);
        // RequestDTO : writer, title, content, thumbnailUri

        String utilStudentId = principal.getName();

        PostDto responseDto = postService.createPost(requestDto, utilStudentId);
        return ResponseEntity.ok(responseDto);
    }

    //    @CrossOrigin
    @PutMapping("/blog/{postId}") // 블로그 수정 - PUT, /api/blog/{postId}
    public ResponseEntity<PostDto> modify(@RequestBody PostDto requestDto,
                                          @PathVariable Long postId,
                                          Principal principal) {
        log.info("-- Modify (title + content) of post --");
        // repository에 대해서 해당 id를 가진 엔티티를 가져오고,
        // 그 엔티티의 내용을 dto 내용으로 수정 및 다시 repository에 저장한다

        log.info("Request Dto = " + requestDto);

        String utilStudentId = principal.getName();
        PostDto responseDto = postService.updatePost(requestDto, postId, utilStudentId);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/blog/{postId}") // 블로그 삭제 - DELETE, /api/blog/{postid}
    public ResponseEntity<Void> delete(@PathVariable Long postId,
                                       Principal principal) {
        log.info("-- Delete Specific Post --");

        String utilStudentId = principal.getName();
        postService.deletePost(postId, utilStudentId);
        return ResponseEntity.noContent().build();
    }
}
