package pl.okazje.project.entities;

import javax.persistence.*;
import java.util.*;

@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conversation_id;

    @OneToMany(mappedBy="conversation")
    private Set<Message> messages;

    @ManyToMany(mappedBy = "conversations")
    private Set<User> users;

    public Conversation() {
    }

    public Long getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(Long conversation_id) {
        this.conversation_id = conversation_id;
    }

    public Set<Message> getMessages() {
        if (messages==null) return new HashSet<>();
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



    public boolean hasNewMessage(User uzytkownik){

        ArrayList<Message> list = new ArrayList<>(getMessages());
        if(list.isEmpty()) return false;
        list.removeIf(o -> (o.getUser().getUser_id().equals(uzytkownik.getUser_id())));
        Collections.sort(list, (o1, o2) -> o2.getCr_date().compareTo(o1.getCr_date()));
        if(!list.isEmpty()&&list.get(0).getStatus().equals(Message.Status.NEW)) return true;
        return false;

    }

    public Message getOtherUserNewMessage(User uzytkownik){

        ArrayList<Message> list = new ArrayList<>(getMessages());
        if(list.isEmpty()) return new Message();
        list.removeIf(o -> (o.getUser().getUser_id()==uzytkownik.getUser_id()));
        if (list.isEmpty()){

            return new Message();
        }
        Collections.sort(list, (o1, o2) -> o2.getCr_date().compareTo(o1.getCr_date()));
        return list.get(0);

    }

    public ArrayList<Message> getMessagesSorted(){


        ArrayList<Message> list = new ArrayList<>(this.getMessages());
        Collections.sort(list, (o1, o2) -> o2.getCr_date().compareTo(o1.getCr_date()));
        return list;

    }

    public Message getNewestMessageObject(){

        ArrayList<Message> list = new ArrayList<>(getMessages());
        if(list.isEmpty()) return new Message();
        Collections.sort(list, (o1, o2) -> o2.getCr_date().compareTo(o1.getCr_date()));
        return list.get(0);

    }

    public User getOtherUser(User uzytkownik){

        ArrayList<User> list = new ArrayList<>(getUsers());
        list.removeIf(o -> o.getUser_id()==uzytkownik.getUser_id());
        return list.get(0);

    }

    public String getNewestMessage(User uzytkownik){

        ArrayList<Message> list = new ArrayList<>(getMessages());
        if(list.isEmpty()) return "";
        Collections.sort(list, (o1, o2) -> o2.getCr_date().compareTo(o1.getCr_date()));
        if(list.get(0).getUser().getUser_id()==uzytkownik.getUser_id()){

            return "Ty: "+ list.get(0).getContent();

        }
        return list.get(0).getContent();

    }
}