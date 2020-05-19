package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.Information;


@Repository
public interface DiscountRepository extends CrudRepository<Discount, Long> {}