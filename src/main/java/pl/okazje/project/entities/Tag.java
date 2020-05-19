package pl.okazje.project.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tag_id;
    private String name;

    @OneToMany(mappedBy="tag")
    private Set<Post> posts;

    @OneToMany(mappedBy="tag")
    private Set<Discount> discounts;

    public Tag() {
    }
}
