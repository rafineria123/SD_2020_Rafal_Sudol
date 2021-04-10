package pl.okazje.project.entities;

import pl.okazje.project.exceptions.DataTooLongException;

import javax.persistence.*;


@Entity
public class Ban {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ban_id;
    @Column(length = 300)
    private String reason = "Powód nie podany.";
    @OneToOne(mappedBy = "ban")
    private User user;

    public Ban() {
    }

    public Long getBan_id() {
        return ban_id;
    }

    public void setBan_id(Long ban_id) {
        this.ban_id = ban_id;
    }

    public String getReason() { return reason; }

    public void setReason(String reason) {
        if(reason.length()>300){
            throw new DataTooLongException(reason.substring(0, Math.min(reason.length(), 50))+"...");
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
