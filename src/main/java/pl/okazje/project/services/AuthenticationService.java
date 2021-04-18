package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.User;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final FindByIndexNameSessionRepository sessionRepository;

    @Autowired
    public AuthenticationService(UserService userService, FindByIndexNameSessionRepository sessionRepository) {
        this.userService = userService;
        this.sessionRepository = sessionRepository;
    }

    public Optional<User> getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("USER") || r.getAuthority().equals("ADMIN"))) {
            return userService.findFirstByLogin(authentication.getName());
        }
        return Optional.empty();

    }

    public boolean isUserLoggedIn(User user){
        Map<String, Session> userSessions = sessionRepository.findByPrincipalName(user.getUsername());
        if (userSessions.isEmpty()) {
            return false;
        } else {
            for(Session session:userSessions.values()){
                if (!session.isExpired()) {
                    return true;
                }
            }
        }
        return false;
    }

}
