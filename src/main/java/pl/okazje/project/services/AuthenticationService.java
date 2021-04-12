package pl.okazje.project.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.User;

import java.util.Optional;

@Service
public class AuthenticationService {

    final private UserService userService;

    public AuthenticationService(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("USER") || r.getAuthority().equals("ADMIN"))) {
            return userService.findFirstByLogin(authentication.getName());
        }
        return Optional.empty();
    }

}
