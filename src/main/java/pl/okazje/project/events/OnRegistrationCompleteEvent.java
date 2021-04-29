package pl.okazje.project.events;

import org.springframework.context.ApplicationEvent;
import pl.okazje.project.entities.User;

import java.util.Locale;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private User user;

    public OnRegistrationCompleteEvent(User user) {
        super(user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}