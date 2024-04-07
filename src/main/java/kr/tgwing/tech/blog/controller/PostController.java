package kr.tgwing.tech.blog.controller;

import kr.tgwing.tech.blog.dto.PostDto;
import kr.tgwing.tech.blog.entity.PostEntity;
import kr.tgwing.tech.blog.repository.PostRepository;
import kr.tgwing.tech.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tgwing.kr")
@RequiredArgsConstructor
public class PostController {

    /*
     * 공지글 가져오기 - GET, /tgwing.kr/info/notice
     * 특정 공지글 가져오기 - GET, /tgwing.kr/info/notice/{id}
     * 공지 내용 검색 - GET, /tgwing.kr/notice/search?text={search}
     * 공지 작성 - POST, /tgwing.kr/notice/post
     * 공지 수정 - PUT, /tgwing.kr/notice/put/{id}
     * 공지 삭제 - DELETE, /tgwing.kr/notice/delete/{id}
     * */
    private final PostService postService;
    private final PostRepository postRepository;
//    private final UserRepository userRepository;

    @GetMapping("/info/notice") // 공지글 가져오기 - GET, /tgwing.kr/info/notice
    public ResponseEntity<List<PostDto>> getAllPosts() {
        System.out.println("-- Retrieve All of Posts (to Front...) --");

        List<PostDto> result = postService.getAllPosts();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/notice/{postId}") // 특정 보기 - GET, /tgwing.kr/notice/{id}
    public ResponseEntity<PostDto> getPost(@PathVariable("postId") Long postId) {
        System.out.println("-- Retreive Specific Post by Id --");
        // 특정 게시글 id에 대한 post 정보를 모아 반환 (id, title, description, writtenDate, ?조회수?

        PostDto post = postService.getPost(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/notice/search") // 공지 내용 검색 - GET, /tgwing.kr/notice/search?text={search}
    public ResponseEntity<List<PostDto>> searchPost(@RequestParam String text)
    {
        System.out.println("-- Retrieve Posts which description has search text --");

        // 입력 파라미터로 넘어온 텍스트를 내용으로 갖고 있는 post를 찾아서 반환
        // 검색 결과 복수개 가능 - List로 전달
        List<PostDto> postDtos = postService.searchPosts(text);
        return ResponseEntity.ok(postDtos);
    }

    @PostMapping("/notice/post") // 공지 작성 - POST, /tgwing.kr/notice/post
    public ResponseEntity<PostDto> post(@RequestBody PostDto requestDto,
                                        @RequestHeader("authorization") String token)
    {
        System.out.println("-- Post new post --");
        System.out.println("Request Dto = " + requestDto);
        // RequestDTO : writer, title, content, thumbnail(선택)

        // dto 가져온 값을 엔티티로 바꾸고 DB에 save
        // 다시 꺼내와서 완성된 객체의 구성요소(id, title, description, writtenDate ...)

        // 유저의 level 확인 필요 - level = MANAGER인 경우에만 작성가능
        // 로그인 되어있지 않은 경우 NORMAL, 로그인 된 경우 MEMBER, 관리자인 경우 MANAGER

        PostDto responseDto = postService.createPost(requestDto, token);
        return ResponseEntity.ok(responseDto);
    }
//    @CrossOrigin
    @PatchMapping ("/notice/modify/{id}") // 공지 수정 - PUT, /tgwing.kr/notice/modify/{id}
    public ResponseEntity<PostDto> modify(@RequestBody PostDto requestDto,
                                       @RequestParam Long userId,
                                       @PathVariable("id") Long id)
    {
        System.out.println("-- Modify (title + content) of post --");
        // repository에 대해서 해당 id를 가진 엔티티를 가져오고,
        // 그 엔티티의 내용을 dto 내용으로 수정 및 다시 repository에 저장한다

        System.out.println("Request Dto = " + requestDto);

        PostDto responseDto = postService.updatePost(requestDto, userId, id);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/notice/delete/{id}") // 공지 삭제 - DELETE, /tgwing.kr/notice/delete/{id}
    public ResponseEntity<Void> delete(@PathVariable("id") Long postId,
                                       @RequestParam Long userId)
    {
        System.out.println("-- Delete Specific Post --");
        // 아이디에 해당하는 글 객체를 그냥 삭제 -> 응답
        postService.deletePost(userId, postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/notice") // 공지 게시판 보기 - GET, /tgwing.kr/notice?page={pp}&length={ll}
    @Transactional
    public ResponseEntity<Page> getPostsInPage(@RequestParam int page,
                                               @RequestParam(defaultValue = "15") int size,
                                               @PageableDefault Pageable pageable) {
        System.out.println("-- Get Posts in Page --");
        // 첫 번째 페이지 page = 0이므로, page-1로 전달 -> 1부터 요청할 수 있도록
        Page<PostDto> postsInPage = postService.findPostsInPage(page-1, size, pageable);

        return ResponseEntity.ok(postsInPage);
    }
}
