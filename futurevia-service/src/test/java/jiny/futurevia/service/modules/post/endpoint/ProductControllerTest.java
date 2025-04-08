package jiny.futurevia.service.modules.post.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jiny.futurevia.service.WithAccount;
import jiny.futurevia.service.modules.post.endpoint.dto.request.ProductCreate;
import jiny.futurevia.service.modules.post.infra.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

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
    @WithAccount("jiny798")
    @DisplayName("상품 등록: 정상작동")
    public void createPost() throws Exception {
        // given
        ProductCreate productCreate = ProductCreate.builder()
                .title("제목입니다.")
                .price(1000L)
                .details("상품설명")
                .isDefect(false)
                .minOrderQuantity(1L)
                .shippingFee(3000L)
                .shippingMethod("택배배송")
                .stockQuantity(2L)
                .build();
        String json = mapper.writeValueAsString(productCreate);

        //when
        mockMvc.perform(post("/api/products")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
//
//        //then
        Assertions.assertEquals(1L, productRepository.count());

//        Product post = postRepository.findAll().get(0);
//        assertEquals("제목입니다.", post.getTitle());
//        assertEquals("내용입니다.", post.getContent());
    }
}