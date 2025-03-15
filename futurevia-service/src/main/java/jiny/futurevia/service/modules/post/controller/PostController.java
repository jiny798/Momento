package jiny.futurevia.service.modules.post.controller;

import jakarta.validation.Valid;
import jiny.futurevia.service.modules.post.dto.request.PostCreate;
import jiny.futurevia.service.modules.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/posts")
    public Map<String,String> post(@RequestBody @Valid PostCreate postCreate ) throws Exception {
        postService.write(postCreate);
        return Map.of();
    }
}
