package pl.okazje.project.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer discount_id;
    private String title;
    private String content;
    private String image_url;
    private Date cr_date;
    private Date expire_date;
    private String status;
    private Double old_price;
    private Double current_price;
    private Double shipment_price;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @OneToMany(mappedBy="discount")
    private Set<Comment> comments;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rating_id")
    private Rating rating;

    @ManyToOne
    @JoinColumn(name="tag_id", nullable=false)
    private Tag tag;

    @ManyToOne
    @JoinColumn(name="shop_id", nullable=false)
    private Shop shop;

    public Discount() {
    }
}
