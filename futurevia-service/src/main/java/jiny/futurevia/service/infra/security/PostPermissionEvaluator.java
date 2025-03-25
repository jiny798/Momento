package jiny.futurevia.service.infra.security;

import jiny.futurevia.service.modules.account.domain.dto.AccountContext;
import jiny.futurevia.service.modules.exception.type.PostNotFound;
import jiny.futurevia.service.modules.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.nio.file.attribute.UserPrincipal;

@Slf4j
@RequiredArgsConstructor
public class PostPermissionEvaluator implements PermissionEvaluator {

    private final PostRepository postRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        var accountContext = (AccountContext) authentication.getPrincipal();

        var post = postRepository.findById((Long) targetId)
                .orElseThrow(PostNotFound::new);

        if(!post.getUserId().equals(accountContext.getAccount().getId())){
            log.error("Account {} not owned by post {}", accountContext.getAccount().getId(), post.getId());
            return false;
        }

        return true;
    }
}
