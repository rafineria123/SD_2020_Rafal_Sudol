package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Token;


@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {

    Token findFirstByToken(String token);

}