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
    public Map<String,String> post(@RequestBody @Valid PostCreate postForm, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError fieldError = fieldErrors.get(0);
            String invalidFieldName = fieldError.getField();
            String fieldErrorMessage = fieldError.getDefaultMessage();

            Map<String,String> error = new HashMap<String,String>();
            error.put(invalidFieldName, invalidFieldName);
            return error;
        }

        String title = postForm.getTitle();

        postService.write(postForm);
        return Map.of();
    }
}
