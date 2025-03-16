package jiny.futurevia.service.modules.post.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jiny.futurevia.service.modules.post.domain.Post;
import jiny.futurevia.service.modules.post.domain.QPost;
import jiny.futurevia.service.modules.post.dto.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static jiny.futurevia.service.modules.post.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(post.id.desc())
                .fetch();

    }
}
