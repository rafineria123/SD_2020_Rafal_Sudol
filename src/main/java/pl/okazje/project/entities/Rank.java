package pl.okazje.project.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rank_id;
    private String name;

    @OneToMany(mappedBy="rank")
    private Set<User> users;

    public Rank() {
    }
}
