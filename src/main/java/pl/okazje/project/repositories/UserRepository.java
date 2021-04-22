package pl.okazje.project.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findFirstByLogin(String login);

    @Query(value = "Select * from User WHERE userId=?1 LIMIT 1" ,
            nativeQuery = true)
    User findFirstByUserIdEquals(int id);

}