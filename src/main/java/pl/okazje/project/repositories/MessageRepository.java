package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Message;


@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {}
