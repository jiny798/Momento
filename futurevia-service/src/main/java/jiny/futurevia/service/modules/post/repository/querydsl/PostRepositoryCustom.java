package jiny.futurevia.service.modules.post.repository.querydsl;

import jiny.futurevia.service.modules.post.domain.Post;
import jiny.futurevia.service.modules.post.dto.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
