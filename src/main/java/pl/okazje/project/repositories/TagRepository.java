package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Tag;


@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

    public Tag findFirstByName(String name);

}