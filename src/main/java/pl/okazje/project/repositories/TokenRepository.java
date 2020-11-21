package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Tag;
import pl.okazje.project.entities.Token;
import pl.okazje.project.entities.User;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {

    public Token findByToken(String token);
    public Token findByUser(User user);

}