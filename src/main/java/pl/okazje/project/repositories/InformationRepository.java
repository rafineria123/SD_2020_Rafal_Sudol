package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Information;


@Repository
public interface InformationRepository extends CrudRepository<Information, Long> {}