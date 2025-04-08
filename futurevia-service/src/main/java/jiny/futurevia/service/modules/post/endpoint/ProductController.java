package jiny.futurevia.service.modules.post.endpoint;


import jakarta.validation.Valid;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.support.CurrentUser;
import jiny.futurevia.service.modules.post.application.ProductService;
import jiny.futurevia.service.modules.post.endpoint.dto.request.ProductCreate;
import jiny.futurevia.service.modules.post.endpoint.dto.request.ProductSearch;
import jiny.futurevia.service.modules.post.endpoint.dto.response.PagingResponse;
import jiny.futurevia.service.modules.post.endpoint.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/products")
    public void post(@CurrentUser Account account, @RequestBody @Valid ProductCreate productCreate) throws Exception {
        productService.write(account.getId(), productCreate);
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
