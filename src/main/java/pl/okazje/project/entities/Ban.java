package pl.okazje.project.entities;

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
    @OneToOne(mappedBy = "ban")
    private Discount discount;
    @OneToOne(mappedBy = "ban")
    private Post post;

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
        if(reason.isEmpty()){
            this.reason = "Powód nie podany.";
        }else{
            this.reason = reason;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
}
