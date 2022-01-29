package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.ratings.Rating;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Long> {



}