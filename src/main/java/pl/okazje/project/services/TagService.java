package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Shop;
import pl.okazje.project.entities.Tag;
import pl.okazje.project.repositories.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> findAll(){
        return tagRepository.findAll();
    }

    public Optional<Tag> findById(Long id){
        return this.tagRepository.findById(id);
    }
}
