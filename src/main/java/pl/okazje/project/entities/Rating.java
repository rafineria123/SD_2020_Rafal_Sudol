package pl.okazje.project.entities;

import javax.persistence.*;

@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rating_id;

    @ManyToOne
    @JoinColumn(name="comment_id", nullable=true)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="discount_id", nullable=true)
    private Discount discount;


    public Rating() {
    }

    public Integer getRating_id() {
        return rating_id;
    }

    public void setRating_id(Integer rating_id) {
        this.rating_id = rating_id;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
}
