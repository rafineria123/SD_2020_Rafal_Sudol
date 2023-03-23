package pl.okazje.project.entities.ratings;

import pl.okazje.project.entities.comments.Comment;
import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.User;

import javax.persistence.*;

@Entity(name = "rating")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ratingId;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    private User user;

    public Rating() {
    }

    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
