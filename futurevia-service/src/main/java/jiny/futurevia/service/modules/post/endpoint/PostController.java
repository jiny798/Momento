package jiny.futurevia.service.modules.post.endpoint;


import jakarta.validation.Valid;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.support.CurrentUser;
import jiny.futurevia.service.modules.post.endpoint.dto.request.PostCreate;
import jiny.futurevia.service.modules.post.endpoint.dto.request.PostEdit;
import jiny.futurevia.service.modules.post.endpoint.dto.request.PostSearch;
import jiny.futurevia.service.modules.post.endpoint.dto.response.PostResponse;
import jiny.futurevia.service.modules.post.application.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/posts")
    public void post(@CurrentUser Account account, @RequestBody @Valid PostCreate postCreate) throws Exception {
        postService.write(account.getId(), postCreate);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long postId) throws Exception {
        return postService.get(postId);
    }

//    @GetMapping("/posts")
//    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) throws Exception {
//        return postService.getList(postSearch);
//    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable(name = "postId") Long postId, @RequestBody @Valid PostEdit postEdit) throws Exception {
        postService.edit(postId, postEdit);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasRole('ROLE_ADMIN') && hasPermission(#postId, 'POST', 'DELETE')")
    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable(name = "postId") Long postId) throws Exception {
        postService.delete(postId);
    }

}
