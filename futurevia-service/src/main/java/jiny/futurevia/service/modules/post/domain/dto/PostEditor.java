package jiny.futurevia.service.modules.post.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {
    private final String title;
    private final String content;

    @Builder
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
