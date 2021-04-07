package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Ban;
import pl.okazje.project.repositories.BanRepository;

@Service
public class BanService {

    private final BanRepository banRepository;

    @Autowired
    public BanService(BanRepository banRepository) {
        this.banRepository = banRepository;
    }

    public void save(Ban ban){
        this.banRepository.save(ban);
    }

}
