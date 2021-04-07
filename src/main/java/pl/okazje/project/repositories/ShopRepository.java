package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Shop;

@Repository
public interface ShopRepository extends CrudRepository<Shop, Long> {

    Shop findFirstByName(String name);

}
