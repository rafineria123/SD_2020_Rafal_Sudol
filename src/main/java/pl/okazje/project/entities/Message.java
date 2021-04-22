package pl.okazje.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.okazje.project.exceptions.DataTooLongException;

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
    private Integer messageId;
    @Column(length = 300)
    private String content;
    private Date createDate;
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    @ManyToOne
    @JoinColumn(name="conversationId", nullable=false)
    @JsonIgnore
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    @JsonIgnore
    private User user;

    public Message() {
        this.content = "";
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if(content.length()>300) throw new DataTooLongException(content);
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
