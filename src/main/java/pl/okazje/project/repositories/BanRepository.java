package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Ban;

@Repository
public interface BanRepository extends CrudRepository<Ban, Long> {}