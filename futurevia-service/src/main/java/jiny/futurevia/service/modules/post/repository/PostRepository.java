package jiny.futurevia.service.modules.post.repository;

import jiny.futurevia.service.modules.post.domain.Post;
import jiny.futurevia.service.modules.post.repository.querydsl.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> , PostRepositoryCustom {

}
