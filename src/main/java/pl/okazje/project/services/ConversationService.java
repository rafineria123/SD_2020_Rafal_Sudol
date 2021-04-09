package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Comment;
import pl.okazje.project.entities.Conversation;
import pl.okazje.project.repositories.ConversationRepository;

import java.util.Optional;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;

    @Autowired
    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Optional<Conversation> findByUsers(int firstUserId, int secondUserId){
            return conversationRepository.findByUsers(firstUserId,secondUserId);
    }

    public Optional<Conversation> findById(Long id){
        return conversationRepository.findById(id);
    }

    public void save(Conversation conversation){
        conversationRepository.save(conversation);
    }
}
