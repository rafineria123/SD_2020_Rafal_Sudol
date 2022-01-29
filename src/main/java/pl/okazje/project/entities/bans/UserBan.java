package pl.okazje.project.entities.bans;

import pl.okazje.project.entities.User;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class UserBan extends Ban {

    @OneToOne(mappedBy = "ban")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}