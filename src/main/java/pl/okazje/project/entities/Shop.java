package pl.okazje.project.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shop_id;
    private String name;
    private String image_url;

    @OneToMany(mappedBy="shop")
    private Set<Discount> discounts;

    @OneToMany(mappedBy="shop")
    private Set<Post> posts;

    public Shop() {
    }

    public Shop(String name, String image_url) {

        this.name = name;
        this.image_url = image_url;

    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Long getShop_id() {
        return shop_id;
    }

    public void setShop_id(Long shop_id) {
        this.shop_id = shop_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Set<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Set<Discount> discounts) {
        this.discounts = discounts;
    }
}
