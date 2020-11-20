package pl.okazje.project.entities;

import javax.persistence.*;


@Entity
public class Ban {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ban_id;
    private String reason = "Powód nie podany.";
    private String duration = "to-do";
    @OneToOne(mappedBy = "ban")
    private User user;

    public Ban() {
    }

    public Integer getBan_id() {
        return ban_id;
    }

    public void setBan_id(Integer ban_id) {
        this.ban_id = ban_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
