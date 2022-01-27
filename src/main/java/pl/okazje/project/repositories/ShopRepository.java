package pl.okazje.project.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.okazje.project.entities.Shop;
import pl.okazje.project.entities.Tag;


import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends CrudRepository<Shop, Long> {

    Optional<Shop> findFirstByName(String name);
    List<Shop> findAll();
    Optional<Shop> findById(Long id);

}

