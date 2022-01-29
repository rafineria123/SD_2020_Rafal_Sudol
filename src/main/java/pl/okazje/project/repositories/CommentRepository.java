package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.comments.Comment;


@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {}
