package jiny.futurevia.service.modules.product.endpoint;


import jakarta.validation.Valid;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.support.CurrentUser;
import jiny.futurevia.service.modules.product.application.ImageService;
import jiny.futurevia.service.modules.product.application.ProductService;
import jiny.futurevia.service.modules.product.endpoint.dto.request.ProductCreate;
import jiny.futurevia.service.modules.product.endpoint.dto.request.ProductSearch;
import jiny.futurevia.service.modules.product.endpoint.dto.response.PagingResponse;
import jiny.futurevia.service.modules.product.endpoint.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ImageService imageService;
    private final String host = "http://localhost:8080";

    /*
     * 상품 등록
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/products")
    public void post(@AuthenticationPrincipal Account account, @RequestBody @Valid ProductCreate productCreate) throws Exception {
        log.info("ProductCreate : {}", productCreate);
        productService.write(account.getId(), productCreate);
    }

    /*
     * 상품 이미지 등록
     */
    @PostMapping("/images")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        String url = imageService.upload(file); // 예: AWS S3, 로컬 경로 등
        return host+url;
    }

    /*
     * 상품 정보
     */
    @GetMapping("/posts/{postId}")
    public ProductResponse get(@PathVariable(name = "postId") Long postId) throws Exception {
        return productService.get(postId);
    }

    /*
     * 상품 리스트
     */
    @GetMapping("/products")
    public PagingResponse<ProductResponse> getList(@ModelAttribute ProductSearch postSearch) throws Exception {
        return productService.getList(postSearch);
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PatchMapping("/products/{productId}")
//    public void edit(@PathVariable(name = "productId") Long productId, @RequestBody @Valid PostEdit postEdit) throws Exception {
//        productService.edit(productId, postEdit);
//    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/products/{productId}")
    public void delete(@PathVariable(name = "productId") Long productId) throws Exception {
        productService.delete(productId);
    }


}
