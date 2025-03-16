package jiny.futurevia.service.modules.post.service;


import jiny.futurevia.service.modules.post.domain.Post;
import jiny.futurevia.service.modules.post.domain.PostEditor;
import jiny.futurevia.service.modules.post.dto.request.PostEdit;
import jiny.futurevia.service.modules.post.dto.request.PostSearch;
import jiny.futurevia.service.modules.post.dto.response.PostResponse;
import jiny.futurevia.service.modules.post.repository.PostRepository;
import jiny.futurevia.service.modules.post.dto.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {

        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        postRepository.delete(post);
    }

}










