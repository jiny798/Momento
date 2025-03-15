package jiny.futurevia.service.modules.post.controller;

import jakarta.validation.Valid;
import jiny.futurevia.service.modules.post.domain.Post;
import jiny.futurevia.service.modules.post.dto.request.PostCreate;
import jiny.futurevia.service.modules.post.dto.response.PostResponse;
import jiny.futurevia.service.modules.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/posts")
    public void post(@RequestBody @Valid PostCreate postCreate ) throws Exception {
        postService.write(postCreate);

    }

    @GetMapping("/api/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long postId) throws Exception {
        return postService.get(postId);
    }

}
