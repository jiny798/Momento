package jiny.futurevia.service.modules.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jiny.futurevia.service.modules.post.domain.Post;
import jiny.futurevia.service.modules.post.dto.request.PostCreate;
import jiny.futurevia.service.modules.post.dto.response.PostResponse;
import jiny.futurevia.service.modules.post.repository.PostRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 생성 : 정상작동")
    public void createPost() throws Exception {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String json = mapper.writeValueAsString(postCreate);

        //when
        mockMvc.perform(post("/api/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());

        //then
        Assertions.assertEquals(1L, postRepository.count());

        Post post = postRepository.findById(1L).get();
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("게시글 생성 : 제목누락")
    public void createPostFailByTitle() throws Exception {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("")
                .content("내용입니다.")
                .build();
        String json = mapper.writeValueAsString(postCreate);

        mockMvc.perform(post("/api/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validations.title").value("제목을 입력하세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("글 1개 조회")
    public void getPost() throws Exception {
        // given
        Post post = Post.builder()
                .title("title")
                .content("content")
                .build();
        postRepository.save(post);

        // when
        mockMvc.perform(get("/api/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andDo(print());

        // then
    }


    @Test
    @DisplayName("글 여러개 조회")
    public void getPostList() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(0, 30).mapToObj(i -> {
            return Post.builder()
                    .title("title-"+ i)
                    .content("content-"+ i)
                    .build();
        }).toList();
        postRepository.saveAll(requestPosts);

        // when
        mockMvc.perform(get("/api/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].title").value("title-29"))
                .andExpect(jsonPath("$[0].content").value("content-29"))
                .andDo(print());

        // then
    }
}