package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Conversation;
import pl.okazje.project.entities.Message;
import pl.okazje.project.entities.User;
import pl.okazje.project.exceptions.ConversationNotFoundException;
import pl.okazje.project.repositories.ConversationRepository;

import java.util.*;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final AuthenticationService authenticationService;
    private final MessageService messageService;
    private final EmailService emailService;
    private final UserService userService;

    public ConversationService(ConversationRepository conversationRepository, AuthenticationService authenticationService, MessageService messageService, EmailService emailService, UserService userService) {
        this.conversationRepository = conversationRepository;
        this.authenticationService = authenticationService;
        this.messageService = messageService;
        this.emailService = emailService;
        this.userService = userService;
    }

    public Optional<Conversation> findByUsers(int firstUserId, int secondUserId) {
        return conversationRepository.findByUsers(firstUserId, secondUserId);
    }

    public Optional<Conversation> findById(Long id) {
        Optional<Conversation> optionalConversation = conversationRepository.findById(id);
        User currentUser = authenticationService.getCurrentUser().get();
        if (optionalConversation.isPresent() && optionalConversation.get().isUserInConversation(currentUser)) {
            Optional<Message> tempMessage = optionalConversation.get().getOtherUserNewMessage(currentUser);
            if (tempMessage.isPresent()) {
                Message lastMessage = tempMessage.get();
                lastMessage.setStatus(Message.Status.SEEN);
                messageService.save(lastMessage);
            }
        }
        return optionalConversation;
    }


    public void save(Conversation conversation) {
        conversationRepository.save(conversation);
    }

    public void sendMessage(User otherUser, String messageContent){
        User currentUser = authenticationService.getCurrentUser().get();
        Optional<Conversation> optionalConversation = this.findByUsers(currentUser.getUserId(),otherUser.getUserId());
        if(!optionalConversation.isPresent()){
            Conversation conversation = new Conversation();
            Set<User> users = new HashSet<User>(){{
                add(currentUser);
                add(otherUser);
            }};
            conversation.setUsers(users);
            this.save(conversation);
            currentUser.getConversations().add(conversation);
            otherUser.getConversations().add(conversation);
            userService.save(currentUser);
            userService.save(otherUser);
        }
        Message message = new Message();
        message.setStatus(Message.Status.NEW);
        message.setContent(messageContent);
        message.setConversation(this.findByUsers(currentUser.getUserId(),otherUser.getUserId()).get());
        message.setCreateDate(new Date());
        message.setUser(currentUser);
        messageService.save(message);
        if (!authenticationService.isUserLoggedIn(otherUser)) {
            emailService.sendEmail(otherUser.getEmail(),
                    "NORGIE - Otrzymales nową wiadomość", "Witaj " + otherUser.getLogin() + ",\n Otrzymałeś nową wiadomość od "
                            + currentUser.getLogin() + "\n Treść wiadomości: " + message.getContent());
        }

    }

    public String[][] getConversationBody(Long id){
        Optional<Conversation> optionalConversation = this.findById(id);
        if(!optionalConversation.isPresent()){
            throw new ConversationNotFoundException(id);
        }
        User currentUser = authenticationService.getCurrentUser().get();
        User otherUser = optionalConversation.get().getOtherUser(currentUser);
        ArrayList<Message> list = this.findByUsers(currentUser.getUserId(), otherUser.getUserId()).get().getMessagesSorted();
        String array[][] = new String[list.size()][2];
        for (int i = 0; i < list.size(); i++) {
            array[i][0] = list.get(i).getContent();
            if (list.get(i).getUser().getUserId() == currentUser.getUserId()) array[i][1] = "right";
            if (list.get(i).getUser().getUserId() == otherUser.getUserId()) array[i][1] = "left";
        }
        return array;
    }

    public String[][] getAllCurrentUserConversationsAsArray(){
        User currentUser = authenticationService.getCurrentUser().get();
        ArrayList<Conversation> list = new ArrayList<>(this.getAllCurrentUserConversations());
        String array[][] = new String[list.size()][6];
        for (int i = 0; i < list.size(); i++) {
            array[i][0] = list.get(i).getOtherUser(currentUser).getLogin();
            array[i][1] = list.get(i).getNewestMessage(currentUser);
            if (list.get(i).hasNewMessage(currentUser)) array[i][2] = "NEW";
            if (!list.get(i).hasNewMessage(currentUser)) array[i][2] = "SEEN";
            array[i][3] = list.get(i).getConversationId().toString();
            array[i][4] = list.get(i).getOtherUser(currentUser).getLogin();
            array[i][5] = list.get(i).getNewestMessage(currentUser);
        }
        return array;
    }

    public Set<Conversation> getAllCurrentUserConversations(){
        return authenticationService.getCurrentUser().get().getConversations();
    }

    public int countConversationsWithNewMessagesForCurrentUser(){
        Set<Conversation> conversationSet = new HashSet<>(getAllCurrentUserConversations());
        int count = 0;
        for(Conversation conversation : conversationSet){
            if(conversation.hasNewMessage(authenticationService.getCurrentUser().get())){
                count++;
            }
        }
        return count;
    }

}
