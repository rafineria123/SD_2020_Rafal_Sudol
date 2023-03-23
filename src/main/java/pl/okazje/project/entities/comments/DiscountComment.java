package pl.okazje.project.entities.comments;

import pl.okazje.project.entities.Discount;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "discountcomment")
public class DiscountComment extends Comment {

    @ManyToOne
    @JoinColumn(name="discountId")
    private Discount discount;

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
}
