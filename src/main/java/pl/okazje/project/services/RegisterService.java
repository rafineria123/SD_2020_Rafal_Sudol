package pl.okazje.project.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import pl.okazje.project.entities.Token;
import pl.okazje.project.entities.User;
import pl.okazje.project.events.OnRegistrationCompleteEvent;

import java.util.*;

@Service
public class RegisterService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final TokenService tokenService;

    public RegisterService(UserService userService, PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher, TokenService tokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.tokenService = tokenService;
    }

    public Map<String, String> registerUser(String login, String password, String repeated_password, String email, String statute){
        Map<String, String> map = new HashMap<>();
        if (!password.equals(repeated_password)) {
            map.put("bad_status", "Hasła muszą się powtarzać w obydwu polach.");
            return map;
        }
        if (!statute.equals("on")) {
            map.put("bad_status", "Musisz zaakceptować regulamin.");
            return map;
        }
        if (login.length() < 5) {
            map.put("bad_status", "Wprowadzony login jest za krótki.");
            return map;
        }
        if (password.length() < 5) {
            map.put("bad_status", "Wprowadzone hasło jest za krótkie.");
            return map;
        }
        if (userService.findFirstByLogin(login).isPresent()) {
            map.put("bad_status", "Ten login jest juz zajęty.");
            return map;
        }
        User tempUser = new User();
        tempUser.setCr_date(new Date());
        tempUser.setLogin(login);
        tempUser.setPassword(passwordEncoder.encode(password));
        tempUser.setEmail(email);
        userService.save(tempUser);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(tempUser));
        map.put("good_status", "Na twój mail została wysłana wiadomość. Może to potrwać kilka minut. Aktywuj swoje konto klikając w link aktywacyjny.");
        return map;
    }

    public Map<String, String> confirmRegistration(String token){
        Map<String, String> map = new HashMap<>();
        Optional<Token> verificationToken = tokenService.findByToken(token);
        if (!verificationToken.isPresent()) {
            map.put("bad_status", "Taki token nie istnieje.");
            return map;
        }
        User user = verificationToken.get().getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.get().getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            map.put("bad_status", "Twój token wygasł.");
            return map;
        }
        user.setEnabled(true);
        userService.save(user);
        map.put("good_status", "Możesz się teraz zalogować.");
        return map;
    }
}
