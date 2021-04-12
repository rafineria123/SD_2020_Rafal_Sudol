package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Shop;
import pl.okazje.project.entities.Tag;
import pl.okazje.project.repositories.ShopRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ShopService {
    private final ShopRepository shopRepository;

    @Autowired
    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    public List<Shop> findAll(){
        return shopRepository.findAll();
    }

    public Optional<Shop> findById(Long id){
        return this.shopRepository.findById(id);
    }

}
