package pl.okazje.project.entities;

import javax.persistence.*;

@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rating_id;
    private Integer likes;
    private Integer dislikes;

    @OneToOne(mappedBy = "rating")
    private Comment comment;

    @OneToOne(mappedBy = "rating")
    private User user;

    @OneToOne(mappedBy = "rating")
    private Discount discount;

    public Rating() {
    }
}
