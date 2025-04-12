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

    /*
     * 상품 등록
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/products")
    public void post(@CurrentUser Account account, @RequestBody @Valid ProductCreate productCreate) throws Exception {
        productService.write(account.getId(), productCreate);
    }

    /*
     * 상품 이미지 등록
     */
    @PostMapping("/images")
    public String uploadImage(@RequestParam MultipartFile files) {
        String url = imageService.upload(files);
        return url;
    }

    @GetMapping("/posts/{postId}")
    public ProductResponse get(@PathVariable(name = "postId") Long postId) throws Exception {
        return productService.get(postId);
    }

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
