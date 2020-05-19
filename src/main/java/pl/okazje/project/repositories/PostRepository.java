package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Post;


@Repository
public interface PostRepository extends CrudRepository<Post, Long> {}