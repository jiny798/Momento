package jiny.futurevia.service.modules.post.endpoint.dto.request;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearch {

    private static final int MAX_PAGE = 999;
    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    public void setPage(Integer page) {
        this.page = page <= 0 ? 1 : min(page, MAX_PAGE);
    }

    public long getOffset() {
        return (long) (page - 1) * min(size, MAX_SIZE);
    }

    public Pageable getPageable() {
        return PageRequest.of(page - 1, size);
    }
}
