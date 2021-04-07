package pl.okazje.project.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.okazje.project.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {


    User findFirstByLogin(String login);


    User findFirstByUser_id(int id);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM spring_session WHERE principal_name=?1" ,
            nativeQuery = true)
    void banUser(String name);

    @Query(value = "Select EXPIRY_TIME FROM spring_session WHERE principal_name=?1" ,
            nativeQuery = true)
    List<String> getUserSession(String name);

}