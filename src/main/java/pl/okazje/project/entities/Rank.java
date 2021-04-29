package pl.okazje.project.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rankId;
    private String name;

    @OneToMany(mappedBy="rank")
    private Set<User> users;

    public Rank() {
    }
}
