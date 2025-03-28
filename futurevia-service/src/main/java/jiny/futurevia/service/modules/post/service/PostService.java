package jiny.futurevia.service.modules.post.service;


import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;
import jiny.futurevia.service.modules.exception.type.PostNotFound;
import jiny.futurevia.service.modules.exception.type.UserNotFound;
import jiny.futurevia.service.modules.post.domain.Post;
import jiny.futurevia.service.modules.post.domain.PostEditor;
import jiny.futurevia.service.modules.post.dto.request.PostEdit;
import jiny.futurevia.service.modules.post.dto.request.PostSearch;
import jiny.futurevia.service.modules.post.dto.response.PostResponse;
import jiny.futurevia.service.modules.post.repository.PostRepository;
import jiny.futurevia.service.modules.post.dto.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    public void write(Long userId, PostCreate postCreate) {
        var user = accountRepository.findById(userId).orElseThrow(UserNotFound::new);

        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

    }

    public List<PostResponse> getList(PostSearch postSearch) {

        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder builder = post.toEditor();

        // TODO: Builder 클래스 내부에서 null 체크해서, null이면 저장되지 않도록 수정 필요
        if(postEdit.getTitle() != null) {
            builder.title(postEdit.getTitle());
        }

        if(postEdit.getContent() != null) {
            builder.content(postEdit.getContent());
        }

        PostEditor postEditor = builder.build();

        post.edit(postEditor);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }

}










