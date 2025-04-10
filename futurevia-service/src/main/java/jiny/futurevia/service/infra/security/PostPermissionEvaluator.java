package jiny.futurevia.service.infra.security;

import jiny.futurevia.service.modules.account.domain.dto.AccountContext;
import jiny.futurevia.service.modules.exception.type.ProductNotFound;
import jiny.futurevia.service.modules.product.infra.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@Slf4j
@RequiredArgsConstructor
public class PostPermissionEvaluator implements PermissionEvaluator {

    private final ProductRepository productRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        var accountContext = (AccountContext) authentication.getPrincipal();

        var post = productRepository.findById((Long) targetId)
                .orElseThrow(ProductNotFound::new);

//        if(!post.getUserId().equals(accountContext.getAccount().getId())){
//            log.error("Account {} not owned by post {}", accountContext.getAccount().getId(), post.getId());
//            return false;
//        }

        return true;
    }
}
