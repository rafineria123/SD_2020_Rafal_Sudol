package pl.okazje.project.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.*;

@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@conversationId")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conversationId;

    @OneToMany(mappedBy = "conversation")
    private Set<Message> messages;

    @ManyToMany(mappedBy = "conversations")
    private Set<User> users;

    public Conversation() {
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Set<Message> getMessages() {
        if (messages == null) return new HashSet<>();
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public boolean hasNewMessage(User user) {
        if(user == null){
            throw new IllegalArgumentException("User argument is a null.");
        }
        ArrayList<Message> userMessages = new ArrayList<>(this.getMessages());
        if (userMessages.isEmpty()) return false;
        userMessages.removeIf(o -> (o.getUser().getUserId().equals(user.getUserId())));
        Collections.sort(userMessages, (o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
        if (!userMessages.isEmpty() && userMessages.get(0).getStatus().equals(Message.Status.NEW)) return true;
        return false;
    }

    public Optional<Message> getSecondUserNewMessage(User currentUser) {
        if(currentUser == null){
            throw new IllegalArgumentException("User argument is a null.");
        }
        LinkedList<Message> tempMessages = new LinkedList<>(this.getMessages());
        if (tempMessages.isEmpty()) return Optional.empty();
        tempMessages.removeIf(o -> (o.getUser().getUserId() == currentUser.getUserId()));
        if (tempMessages.isEmpty()) {
            return Optional.empty();
        }
        Collections.sort(tempMessages, (o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
        return Optional.of(tempMessages.getFirst());
    }

    public ArrayList<Message> getMessagesSorted() {
        ArrayList<Message> conversationMessages = new ArrayList<>(this.getMessages());
        Collections.sort(conversationMessages, (o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
        return conversationMessages;
    }

    public Message getNewestMessage() {
        ArrayList<Message> conversationMessages = this.getMessagesSorted();
        if (conversationMessages.isEmpty()) throw new NullPointerException("This conversation has no messages.");
        return conversationMessages.get(0);
    }

    public User getSecondUser(User user) {
        if(user == null){
            throw new IllegalArgumentException("User argument is a null.");
        }
        ArrayList<User> allUsers = new ArrayList<>(getUsers());
        allUsers.removeIf(o -> o.getUserId() == user.getUserId());
        return allUsers.get(0);
    }

    public boolean isUserInConversation(User user) {
        return this.getUsers().contains(user);
    }
}