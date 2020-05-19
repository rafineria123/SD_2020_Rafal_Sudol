package pl.okazje.project.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer post_id;
    private String content;
    private String status;
    private Date cr_date;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @OneToMany(mappedBy="post")
    private Set<Comment> comments;

    @ManyToOne
    @JoinColumn(name="tag_id", nullable=false)
    private Tag tag;

    public Post() {
    }
}
