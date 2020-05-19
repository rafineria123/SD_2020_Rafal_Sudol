package pl.okazje.project.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;
    private String login;
    private String password;
    private String nickname;
    private String email;
    private String status;
    private Date cr_date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "information_id")
    private Information information;

    @ManyToOne
    @JoinColumn(name="rank_id", nullable=false)
    private Rank rank;

    @OneToMany(mappedBy="user")
    private Set<Discount> discounts;

    @OneToMany(mappedBy="user")
    private Set<Post> posts;

    @OneToMany(mappedBy="user")
    private Set<Comment> comments;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "User_Message",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "message_id")
    )
    private Set<Message> messages;



    public User() {
    }
}
