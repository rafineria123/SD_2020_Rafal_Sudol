package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Rank;
import pl.okazje.project.entities.User;

@Repository
public interface RankRepository extends CrudRepository<Rank, Long> {}