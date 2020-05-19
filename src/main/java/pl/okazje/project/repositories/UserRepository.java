package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {}