package pl.okazje.project.entities.bans;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Ban {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long banId;
    @Column(length = 300)
    private String reason;

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
}
