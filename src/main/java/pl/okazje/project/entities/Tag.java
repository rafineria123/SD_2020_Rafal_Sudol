package pl.okazje.project.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tag_id;
    private String name;


    @OneToMany(mappedBy="tag")
    private Set<Discount> discounts;

    @OneToMany(mappedBy="tag")
    private Set<Post> posts;


    public Tag() {
    }

    public Tag(String name) {

        this.name = name;

    }

    public Long getTag_id() {
        return tag_id;
    }

    public void setTag_id(Long tag_id) {
        this.tag_id = tag_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Set<Discount> discounts) {
        this.discounts = discounts;
    }
}
