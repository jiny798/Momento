package jiny.futurevia.service.modules.post.domain;

import jakarta.persistence.*;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne
    @JoinColumn
    Account account;

    @Builder
    public Post(String title, String content, Account account) {
        this.title = title;
        this.content = content;
        this.account = account;
    }

    public void edit(PostEditor postEditor) {
        this.title = postEditor.getTitle();
        this.content = postEditor.getContent();
    }

    public PostEditor.PostEditorBuilder toEditor() {
        return PostEditor.builder()
                .title(this.title)
                .content(this.content);
    }

    public Long getUserId(){
        return this.account.getId();
    }

}







