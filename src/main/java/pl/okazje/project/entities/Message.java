package pl.okazje.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Message {
    public enum Status {
        SEEN,
        NEW
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer message_id;
    @Column(length = 300)
    private String content = "";
    private Date cr_date;
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    @ManyToOne
    @JoinColumn(name="conversation_id", nullable=false)
    @JsonIgnore
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @JsonIgnore
    private User user;

    public Message() {
    }

    public Message(String content, Date cr_date, Status status, Conversation conversation, User user) {

        this.content = content;
        this.cr_date = cr_date;
        this.status = status;
        this.conversation = conversation;
        this.user = user;

    }

    public Integer getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Integer message_id) {
        this.message_id = message_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCr_date() {
        return cr_date;
    }

    public void setCr_date(Date cr_date) {
        this.cr_date = cr_date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
