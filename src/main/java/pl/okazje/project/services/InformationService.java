package pl.okazje.project.services;

import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Information;
import pl.okazje.project.repositories.InformationRepository;

@Service
public class InformationService {
    private final InformationRepository informationRepository;

    public InformationService(InformationRepository informationRepository) {
        this.informationRepository = informationRepository;
    }

    public void save(Information  information){
        this.informationRepository.save(information);
    }
}
