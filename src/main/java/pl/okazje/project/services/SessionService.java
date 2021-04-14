package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.User;

import java.util.Collection;
import java.util.Optional;

@Service
public class SessionService {

    private final FindByIndexNameSessionRepository sessionRepository;

    @Autowired
    public SessionService(FindByIndexNameSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Optional<Session> findCurrentSessionForUser(User user){
        Collection<? extends Session> usersSessions = this.sessionRepository.findByPrincipalName(user.getUsername()).values();
        for (Session s:usersSessions) {
            if(!s.isExpired()){
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }
}
