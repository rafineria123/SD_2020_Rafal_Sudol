package pl.okazje.project.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shop_id;
    private String name;
    private String discount_link;

    @OneToMany(mappedBy="shop")
    private Set<Discount> discounts;

    public Shop() {
    }
}
