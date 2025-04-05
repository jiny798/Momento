package jiny.futurevia.service.modules.post.application;


import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;
import jiny.futurevia.service.modules.exception.type.ProductNotFound;
import jiny.futurevia.service.modules.exception.type.UserNotFound;
import jiny.futurevia.service.modules.post.domain.Product;
import jiny.futurevia.service.modules.post.endpoint.dto.request.ProductCreate;
import jiny.futurevia.service.modules.post.endpoint.dto.request.ProductSearch;
import jiny.futurevia.service.modules.post.endpoint.dto.response.PagingResponse;
import jiny.futurevia.service.modules.post.endpoint.dto.response.ProductResponse;
import jiny.futurevia.service.modules.post.infra.repository.ProductRepository;
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

    public void write(Long userId, ProductCreate productCreate) {
        var user = accountRepository.findById(userId).orElseThrow(UserNotFound::new);

        Product product = Product.from(productCreate);

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










