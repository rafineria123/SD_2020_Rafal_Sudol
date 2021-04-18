package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Message;
import pl.okazje.project.repositories.MessageRepository;



@Service
public class MessageService {
    private final MessageRepository messageRepository;

    private final AuthenticationService authenticationService;

    @Autowired
    public MessageService(MessageRepository messageRepository, AuthenticationService authenticationService) {
        this.messageRepository = messageRepository;
        this.authenticationService = authenticationService;
    }

    public void save(Message message){
        this.messageRepository.save(message);
    }

}
