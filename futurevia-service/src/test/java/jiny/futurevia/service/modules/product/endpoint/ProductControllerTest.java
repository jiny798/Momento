package jiny.futurevia.service.modules.product.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jiny.futurevia.service.WithAccount;
import jiny.futurevia.service.modules.product.application.ImageService;
import jiny.futurevia.service.modules.product.domain.Product;
import jiny.futurevia.service.modules.product.endpoint.dto.request.ProductCreate;
import jiny.futurevia.service.modules.product.infra.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
    private ImageService imageService;

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
                .title("제목입니다")
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
        assertEquals(1L, productRepository.count());

        Product post = productRepository.findAll().get(0);
        assertEquals("제목입니다", post.getTitle());

    }

    @Test
    @WithAccount("jiny798")
    @DisplayName("상품 등록: 이미지 등록")
    public void image() throws Exception {
        // 테스트 리소스 경로: src/test/resources/test-image.jpg
        ClassPathResource resource = new ClassPathResource("test-image.JPG");
        InputStream inputStream = resource.getInputStream();

        MockMultipartFile file = new MockMultipartFile(
                "files",
                "test-image.jpg",
                "image/jpeg",
                inputStream
        );

        // 테스트 요청
        mockMvc.perform(multipart("/api/images").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/uploads")));


    }
}