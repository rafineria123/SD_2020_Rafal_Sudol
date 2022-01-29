package pl.okazje.project.entities.ratings;

import pl.okazje.project.entities.Discount;
import pl.okazje.project.entities.comments.Comment;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DiscountRating extends Rating{

    @ManyToOne
    @JoinColumn(name="discountId", nullable=true)
    private Discount discount;

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
}