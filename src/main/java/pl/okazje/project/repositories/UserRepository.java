package pl.okazje.project.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.okazje.project.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT * FROM user where login =?1 LIMIT 1" ,
            nativeQuery = true)
    public User findUserByLogin(String login);

    @Query(value = "SELECT * FROM user where user_id =?1 LIMIT 1" ,
            nativeQuery = true)
    public User findUserById(int id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM spring_session WHERE principal_name=?1" ,
            nativeQuery = true)
    public void banUser(String name);

}