package pl.okazje.project.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT * FROM user where login =?1 LIMIT 1" ,
            nativeQuery = true)
    public User findUserByLogin(String login);

}