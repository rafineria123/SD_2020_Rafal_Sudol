package pl.okazje.project.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.okazje.project.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {


    Optional<User> findFirstByLogin(String login);

    @Query(value = "Select * from User WHERE user_id=?1 LIMIT 1" ,
            nativeQuery = true)
    User findFirstByUser_idEquals(int id);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM spring_session WHERE principal_name=?1" ,
            nativeQuery = true)
    void deleteSessionWhereUsernameEquals(String name);

    @Query(value = "Select EXPIRY_TIME FROM spring_session WHERE principal_name=?1" ,
            nativeQuery = true)
    List<String> findExpiry_timeByUsername(String name);

}