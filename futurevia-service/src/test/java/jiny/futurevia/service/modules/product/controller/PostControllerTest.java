package jiny.futurevia.service.modules.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jiny.futurevia.service.modules.product.infra.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }


    @Test
    @DisplayName("글 1개 조회")
    public void getPost() throws Exception {
        // given
//        Post post = Post.builder()
//                .title("title")
//                .content("content")
//                .build();
//        postRepository.save(post);
//
//        // when
//        mockMvc.perform(get("/api/posts/{postId}", post.getId())
//                        .contentType(APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(post.getId()))
//                .andExpect(jsonPath("$.title").value("title"))
//                .andExpect(jsonPath("$.content").value("content"))
//                .andDo(print());

        // then
    }

    @Test
    @DisplayName("존재하지 않는 글 조회")
    public void getPostException() throws Exception {
//        // when
//        mockMvc.perform(get("/api/posts/{postId}", 1)
//                        .contentType(APPLICATION_JSON)
//                )
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.code").value("404"))
//                .andDo(print());

    }

    @Test
    @DisplayName("글 여러개 조회")
    public void getPostList() throws Exception {
        // given
//        List<Post> requestPosts = IntStream.range(0, 30).mapToObj(i -> {
//            return Post.builder()
//                    .title("title-" + i)
//                    .content("content-" + i)
//                    .build();
//        }).toList();
//        postRepository.saveAll(requestPosts);
//
//        // when
//        mockMvc.perform(get("/api/posts?page=1&size=10")
//                        .contentType(APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(10))
//                .andExpect(jsonPath("$[0].id").value(30))
//                .andExpect(jsonPath("$[0].title").value("title-29"))
//                .andExpect(jsonPath("$[0].content").value("content-29"))
//                .andDo(print());
    }

    @Test
    @DisplayName("글 수정")
    public void editPost() throws Exception {
        // given
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
//        mockMvc.perform(get("/api/posts/{postId}", post.getId())
//                        .contentType(APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(postEdit))
//                )
//                .andExpect(status().isOk())
//                .andDo(print());

    }

    @Test
    @DisplayName("없는 글 수정")
    public void editPostException() throws Exception {
        // given
//        PostEdit postEdit = PostEdit.builder()
//                .title("title2")
//                .content("content2")
//                .build();
//
//        // when
//        mockMvc.perform(get("/api/posts/{postId}", 1)
//                        .contentType(APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(postEdit))
//                )
//                .andExpect(status().isNotFound())
//                .andDo(print());

    }

    @Test
    @WithMockUser(username = "jiny@abc.com" , roles = {"ADMIN"}, password = "jiny1234!")
    @DisplayName("글 삭제")
    public void deletePost() throws Exception {
        // given
//        Post post = Post.builder()
//                .title("title")
//                .content("content")
//                .build();
//        postRepository.save(post);
//
//        // when
//        mockMvc.perform(delete("/api/posts/{postId}", post.getId())
//                        .contentType(APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andDo(print());

    }



}