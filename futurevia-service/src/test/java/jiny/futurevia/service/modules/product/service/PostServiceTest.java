package jiny.futurevia.service.modules.product.service;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {
//
//    @Autowired
//    private PostService postService;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @BeforeEach
//    void setUp() {
//        postRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("글 작성")
//    void test() throws Exception {
//        PostCreate postCreate = PostCreate.builder()
//                .title("제목입니다.")
//                .content("내용입니다.")
//                .build();
//
//        postService.write(postCreate);
//
//        Assertions.assertEquals(1L, postRepository.count());
//        Post post = postRepository.findAll().get(0);
//        assertEquals("제목입니다.", post.getTitle());
//        assertEquals("내용입니다.", post.getContent());
//    }
//
//    @Test
//    @DisplayName("글 1개 조회")
//    public void getPost() throws Exception {
//        // given
//        Post requestPost = Post.builder()
//                .title("title")
//                .content("content")
//                .build();
//        postRepository.save(requestPost);
//
//        // when
//        PostResponse response = postService.get(requestPost.getId());
//
//        // then
//        Assertions.assertNotNull(response);
//        assertEquals("title", response.getTitle());
//        assertEquals("content", response.getContent());
//    }
//
//    @Test
//    @DisplayName("없는 글 조회")
//    public void getPostException() throws Exception {
//        // given
//        Post requestPost = Post.builder()
//                .title("title")
//                .content("content")
//                .build();
//        postRepository.save(requestPost);
//
//        // when
//        // then
//        ProductNotFound e = Assertions.assertThrows(ProductNotFound.class,
//                () -> postService.get(requestPost.getId() + 1), "예외가 발생하지 않음");
//    }
//
//
//    @Test
//    @DisplayName("게시글 페이지 조회")
//    public void test3() throws Exception {
//        // given
//        List<Post> requestPosts = IntStream.range(0, 20).mapToObj(i -> {
//            return Post.builder()
//                    .title("title-" + i)
//                    .content("content-" + i)
//                    .build();
//        }).collect(Collectors.toList());
//        postRepository.saveAll(requestPosts);
//
//        ProductSearch productSearch = ProductSearch.builder()
//                .page(1)
//                .build();
//
//        // when
//        List<PostResponse> postList = postService.getList(productSearch);
//
//        // then
//        assertEquals(10, postList.size());
//        assertEquals("title-19", postList.get(0).getTitle());
//    }
//
//    @Test
//    @DisplayName("게시글 수정")
//    public void editPost() throws Exception {
//        // given
//        Post post = Post.builder()
//                .title("title")
//                .content("content")
//                .build();
//        postRepository.save(post);
//
//        PostEdit postEdit = PostEdit.builder()
//                .title("title2")
//                .content("content2")
//                .build();
//
//        // when
//        postService.edit(post.getId(), postEdit);
//
//        // then
//        Post changedPost = postRepository.findById(post.getId())
//                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다"));
//        Assertions.assertEquals("title2", changedPost.getTitle());
//        Assertions.assertEquals("content2", changedPost.getContent());
//    }
//
//
//    @Test
//    @DisplayName("게시글 수정")
//    public void deletePost() throws Exception {
//        // given
//        Post post = Post.builder()
//                .title("title")
//                .content("content")
//                .build();
//        postRepository.save(post);
//
//        // when
//        postService.delete(post.getId());
//
//        // then
//        Assertions.assertEquals(0, postRepository.count());
//    }
//

}