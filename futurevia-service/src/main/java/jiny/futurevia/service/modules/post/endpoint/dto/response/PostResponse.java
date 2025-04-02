package jiny.futurevia.service.modules.post.endpoint.dto.response;

import jiny.futurevia.service.modules.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class  PostResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
    }

//    @Builder
//    public PostResponse(Long id, String title, String content) {
//        this.id = id;
//        this.title = title;
//        this.content = content;
//        this.createdAt = post.getCreatedAt();
//    }
}
