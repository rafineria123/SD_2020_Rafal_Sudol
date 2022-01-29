package pl.okazje.project.entities.bans;

import pl.okazje.project.entities.Discount;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class DiscountBan extends Ban {

    @OneToOne(mappedBy = "ban")
    private Discount discount;

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

}
