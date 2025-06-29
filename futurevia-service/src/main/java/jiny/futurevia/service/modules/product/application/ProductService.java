package jiny.futurevia.service.modules.product.application;


import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;
import jiny.futurevia.service.modules.product.endpoint.dto.request.RequestProduct;
import jiny.futurevia.service.modules.product.exception.CategoryNotFound;
import jiny.futurevia.service.modules.product.exception.ProductNotFound;
import jiny.futurevia.service.modules.account.exception.UserNotFound;
import jiny.futurevia.service.modules.product.domain.Product;
import jiny.futurevia.service.modules.product.endpoint.dto.request.ProductSearch;
import jiny.futurevia.service.modules.product.endpoint.dto.response.PagingResponse;
import jiny.futurevia.service.modules.product.endpoint.dto.response.ProductResponse;
import jiny.futurevia.service.modules.product.infra.repository.CategoryRepository;
import jiny.futurevia.service.modules.product.infra.repository.ProductRepository;
import jiny.futurevia.service.modules.product.support.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public void saveProduct(Long userId, RequestProduct requestProduct) {
        var account = accountRepository.findById(userId).orElseThrow(UserNotFound::new);
        var category = categoryRepository.findById(requestProduct.getCategoryId()).orElseThrow(CategoryNotFound::new);

        Product product = productMapper.toEntity(requestProduct);
        product.setAccount(account);
        product.setCategory(category);

        productRepository.save(product);
    }

    public ProductResponse get(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFound::new);

        return new ProductResponse(product);

    }

    public PagingResponse<ProductResponse> getList(ProductSearch productSearch) {
        Page<Product> postPage = productRepository.getList(productSearch);
        return new PagingResponse<>(postPage, ProductResponse.class);
    }

//    @Transactional
//    public void edit(Long id, PostEdit postEdit) {
//        Post post = ProductRepository.findById(id)
//                .orElseThrow(ProductNotFound::new);
//
//        PostEditor.PostEditorBuilder builder = post.toEditor();
//
//        if(postEdit.getTitle() != null) {
//            builder.title(postEdit.getTitle());
//        }
//
//        if(postEdit.getContent() != null) {
//            builder.content(postEdit.getContent());
//        }
//
//        PostEditor postEditor = builder.build();
//
//        post.edit(postEditor);
//    }

    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFound::new);

        productRepository.delete(product);
    }

}










