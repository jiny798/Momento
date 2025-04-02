package jiny.futurevia.service.modules.post.infra.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jiny.futurevia.service.modules.post.domain.Post;
import jiny.futurevia.service.modules.post.endpoint.dto.request.PostSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static jiny.futurevia.service.modules.post.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        long total = jpaQueryFactory.select(post.count())
                .from(post)
                .fetchFirst();

        List<Post> items = jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(post.id.desc())
                .fetch();

        return new PageImpl<>(items, postSearch.getPageable(), total);

    }
}
