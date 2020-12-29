package pl.okazje.project.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.Post;

import java.util.List;


@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    @Query(value = "SELECT * from post where status = ?1" +
            " ORDER BY creationdate DESC" ,
            nativeQuery = true)
    public List<Post> sortPostsByDateWithGivenStatus(String status);

}