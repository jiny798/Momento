package jiny.futurevia.service.modules.post.endpoint.dto.request;

import lombok.*;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSearch {

    private static final int MAX_SIZE = 1000;

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    public long getOffset() {
        return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    }

    public Pageable getPageable() {
        return (Pageable) PageRequest.of(page - 1, size);
    }
}
