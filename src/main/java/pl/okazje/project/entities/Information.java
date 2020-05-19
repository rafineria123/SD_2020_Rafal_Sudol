package pl.okazje.project.entities;

import javax.persistence.*;

@Entity
public class Information {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer information_id;
    private String name;
    private String surname;
    private String description;
    @OneToOne(mappedBy = "information")
    private User user;

    public Information() {
    }

}
