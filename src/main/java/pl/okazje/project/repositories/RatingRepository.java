package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Rating;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Long> {



}