package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Rank;

@Repository
public interface RankRepository extends CrudRepository<Rank, Long> {}