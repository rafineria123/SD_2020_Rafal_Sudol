package pl.okazje.project.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer conversation_id;


    @OneToMany(mappedBy="conversation")
    private Set<Message> messages;

    @ManyToMany(mappedBy = "conversations")
    private Set<User> users;

    public Conversation() {
    }

    public Integer getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(Integer conversation_id) {
        this.conversation_id = conversation_id;
    }

    public Set<Message> getMessages() {
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
}