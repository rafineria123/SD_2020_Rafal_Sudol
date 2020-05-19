package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Comment;
import pl.okazje.project.entities.Discount;


@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {}
