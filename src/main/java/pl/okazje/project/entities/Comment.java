package pl.okazje.project.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer comment_id;
    private String content;
    private Integer upper_comment_id;
    private Date cr_date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rating_id")
    private Rating rating;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=true)
    private User user;

    @ManyToOne
    @JoinColumn(name="discount_id", nullable=true)
    private Discount discount;

    @ManyToOne
    @JoinColumn(name="post_id", nullable=true)
    private Post post;

    public Comment() {
    }
}
