package pl.okazje.project.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Information implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer information_id;
    @Column(length = 15)
    private String name = "";
    @Column(length = 15)
    private String surname = "";
    @Column(length = 300)
    private String description = "";
    @OneToOne(mappedBy = "information")
    private User user;

    public Information() {
    }

    public Integer getInformation_id() {
        return information_id;
    }

    public void setInformation_id(Integer information_id) {
        this.information_id = information_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
