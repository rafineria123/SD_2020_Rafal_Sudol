package pl.okazje.project.entities;

import pl.okazje.project.exceptions.DataTooLongException;

import javax.persistence.*;


@Entity
public class Ban {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long banId;
    @Column(length = 300)
    private String reason;
    @OneToOne(mappedBy = "ban")
    private User user;

    public Ban() {
        this.reason = "Powód nie podany.";
    }

    public Long getBanId() {
        return banId;
    }

    public void setBanId(Long banId) {
        this.banId = banId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        if (reason.length() > 300) {
            throw new DataTooLongException(reason.substring(0, Math.min(reason.length(), 50)) + "...");
        }
        this.reason = reason;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
