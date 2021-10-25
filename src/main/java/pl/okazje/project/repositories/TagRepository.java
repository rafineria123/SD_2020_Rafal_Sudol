package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Tag;

import java.util.List;
import java.util.Optional;


@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

    Optional<Tag> findFirstByName(String name);
    List<Tag> findAll();
    Optional<Tag> findById(Long id);

}