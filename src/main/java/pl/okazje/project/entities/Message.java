package pl.okazje.project.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer message_id;
    private String content;
    private Date cr_date;

    @ManyToMany(mappedBy = "messages")
    private Set<User> users;

    public Message() {
    }

}
