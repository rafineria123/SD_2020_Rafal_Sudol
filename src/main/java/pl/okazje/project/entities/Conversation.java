package pl.okazje.project.entities;

import javax.persistence.*;
import java.util.*;

@Entity
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
        ArrayList<Message> list = new ArrayList<>(getMessages());
        if (list.isEmpty()) return false;
        list.removeIf(o -> (o.getUser().getUserId().equals(user.getUserId())));
        Collections.sort(list, (o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
        if (!list.isEmpty() && list.get(0).getStatus().equals(Message.Status.NEW)) return true;
        return false;
    }

    public Optional<Message> getOtherUserNewMessage(User currentUser) {
        LinkedList<Message> tempMessages = new LinkedList<>(this.messages);
        if (tempMessages.isEmpty()) return Optional.empty();
        tempMessages.removeIf(o -> (o.getUser().getUserId() == currentUser.getUserId()));
        if (tempMessages.isEmpty()) {
            return Optional.empty();
        }
        Collections.sort(tempMessages, (o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
        return Optional.of(tempMessages.getFirst());
    }

    public ArrayList<Message> getMessagesSorted() {
        ArrayList<Message> list = new ArrayList<>(this.getMessages());
        Collections.sort(list, (o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
        return list;
    }

    public Message getNewestMessageObject() {
        ArrayList<Message> list = new ArrayList<>(getMessages());
        if (list.isEmpty()) return new Message();
        Collections.sort(list, (o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
        return list.get(0);
    }

    public User getOtherUser(User user) {
        ArrayList<User> list = new ArrayList<>(getUsers());
        list.removeIf(o -> o.getUserId() == user.getUserId());
        return list.get(0);
    }

    public String getNewestMessage(User user) {
        ArrayList<Message> list = new ArrayList<>(getMessages());
        if (list.isEmpty()) return "";
        Collections.sort(list, (o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
        if (list.get(0).getUser().getUserId() == user.getUserId()) {
            return "Ty: " + list.get(0).getContent();
        }
        return list.get(0).getContent();
    }

    public boolean isUserInConversation(User user) {
        return this.getUsers().contains(user);
    }
}