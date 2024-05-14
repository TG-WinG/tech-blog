package kr.tgwing.tech.blog.controller;

import kr.tgwing.tech.blog.dto.PostDto;
import kr.tgwing.tech.blog.exception.PathHasNoPostIdException;
import kr.tgwing.tech.blog.service.PostServiceImpl;
import kr.tgwing.tech.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class PostController {

    /*
     * 블로그 전체 가져오기 - GET, /tgwing.kr/blog
     * 특정 블로그 가져오기 - GET, /tgwing.kr/blog/{id}
     * 블로그 내용 검색 - GET, /tgwing.kr/blog/search?text={search}
     * 블로그 작성 - POST, /tgwing.kr/blog/post
     * 블로그 수정 - PUT, /tgwing.kr/blog/modify/{id}
     * 블로그 삭제 - DELETE, /tgwing.kr/blog/delete/{id}
     * */
    private final PostServiceImpl postService;

    @GetMapping("/blog") // 블로그 전체 가져오기 - GET, /tgwing.kr/notice
    public ResponseEntity<Page> getAllPostswithSearch(@RequestParam(value = "text", required = false) String text,
                                                      @PageableDefault Pageable pageable) {
        System.out.println("-- Retrieve All of Posts --");

        Page<PostDto> postsInPage = postService.getPostsInPage(text, pageable);

        return ResponseEntity.ok(postsInPage);
    }
//    @GetMapping("/blog/page") // 공지 게시판 보기 - GET, /tgwing.kr/blog/page?page={pp}&length={ll}
//    @Transactional
//    public ResponseEntity<Page> getPostsInPage(@RequestParam int page,
//                                               @RequestParam(defaultValue = "15") int size,
//                                               @PageableDefault Pageable pageable) {
//        System.out.println("-- Get Posts in Page --");
//        // 첫 번째 페이지 page = 0이므로, page-1로 전달 -> 1부터 요청할 수 있도록
//        Page<PostDto> postsInPage = postService.findPostsInPage(page-1, size, pageable);
//
//        return ResponseEntity.ok(postsInPage);
//    }

    @GetMapping("/blog/{postId}") // 특정 블로그 가져오기 - GET, /tgwing.kr/blog/{postId}
    public ResponseEntity<PostDto> getPost(@PathVariable(required = false, name = "postId") Optional<Long> optional) {
        System.out.println("-- Retreive Specific Post by Id --");
        // 특정 게시글 id에 대한 post 정보를 모아 반환

        Long postId = optional.orElseThrow(PathHasNoPostIdException::new);

        PostDto post = postService.getPost(postId);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/blog") // 블로그 작성 - POST, /tgwing.kr/blog/post
    public ResponseEntity<PostDto> post(@RequestBody PostDto requestDto,
                                        Principal principal)
    {
        System.out.println("-- Post new post --");
        System.out.println("Request Dto = " + requestDto);
        // RequestDTO : writer, title, content, thumbnailUri

        String utilStudentId = principal.getName();

        PostDto responseDto = postService.createPost(requestDto, utilStudentId);
        return ResponseEntity.ok(responseDto);
    }
//    @CrossOrigin
    @PutMapping ("/blog/{postId}") // 블로그 수정 - PUT, /tgwing.kr/blog/modify/{id}
    public ResponseEntity<PostDto> modify(@RequestBody PostDto requestDto,
                                          @PathVariable Long postId,
                                          Principal principal)
    {
        System.out.println("-- Modify (title + content) of post --");
        // repository에 대해서 해당 id를 가진 엔티티를 가져오고,
        // 그 엔티티의 내용을 dto 내용으로 수정 및 다시 repository에 저장한다

        System.out.println("Request Dto = " + requestDto);

        String utilStudentId = principal.getName();
        PostDto responseDto = postService.updatePost(requestDto, postId, utilStudentId);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/blog/{postId}") // 블로그 삭제 - DELETE, /tgwing.kr/blog/delete/{id}
    public ResponseEntity<Void> delete(@PathVariable Long postId,
                                       Principal principal)
    {
        System.out.println("-- Delete Specific Post --");

        String utilStudentId = principal.getName();
        postService.deletePost(postId, utilStudentId);
        return ResponseEntity.noContent().build();
    }
}
