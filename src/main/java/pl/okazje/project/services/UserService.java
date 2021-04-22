package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Ban;
import pl.okazje.project.entities.Information;
import pl.okazje.project.entities.Token;
import pl.okazje.project.entities.User;
import pl.okazje.project.repositories.UserRepository;

import java.text.MessageFormat;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BanService banService;
    private final TokenService tokenService;
    private final SessionService sessionService;
    private final EmailService emailService;
    private final InformationService informationService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BanService banService, TokenService tokenService, SessionService sessionService,
                       EmailService emailService, InformationService informationService, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.banService = banService;
        this.tokenService = tokenService;
        this.sessionService = sessionService;
        this.emailService = emailService;
        this.informationService = informationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.findFirstByLogin(s);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UsernameNotFoundException(MessageFormat.format("User with login {0} cannot be found.", s));
        }
    }


    public User getUser(String verificationToken) {
        User user = tokenService.findByToken(verificationToken).get().getUser();
        return user;
    }

    public void createVerificationToken(User user, String token) {
        Token myToken = new Token();
        myToken.setToken(token);
        myToken.setUser(user);
        tokenService.save(myToken);
        user.setToken(myToken);
        this.save(user);
    }

    public void changeUserDetails(User user, String name, String surname, String email){
        user.setEmail(email);
        Information info = new Information();
        if (user.getInformation() == null) {
            info.setName(name);
            info.setSurname(surname);
            info.setUser(user);
        } else {
            info = user.getInformation();
            info.setName(name);
            info.setSurname(surname);
        }
        informationService.save(info);
        user.setInformation(info);
        this.save(user);
    }

    public void changeUserDescription(User user, String description){
        Information info;
        if (user.getInformation() == null) {
            info = new Information();
        } else {
            info = user.getInformation();
        }
        info.setDescription(description);
        this.informationService.save(info);
        user.setInformation(info);
        this.save(user);
    }

    public void changePassword(User user, String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        this.save(user);
        emailService.sendEmail(user.getEmail(), "Norgie - Hasło zostało zmienione",
                "Witaj " + user.getLogin() + ", \n hasło do twojego konta zostało zmienione.\n\nJeśli " +
                        "nie zostało ono zaktualizowane przez Ciebie, powiadom nas o tym jak najszybciej.");
    }

    public Optional<User> findFirstByLogin(String login){
        return userRepository.findFirstByLogin(login);
    }

    public void save(User user){
        this.userRepository.save(user);
    }

    public void banUser(int userID, String reason){
        User user = userRepository.findFirstByUserIdEquals(userID);
        Ban ban = new Ban();
        ban.setReason(reason);
        ban.setUser(user);
        banService.save(ban);
        user.setBan(ban);
        this.save(user);
        Optional<Session> optionalSession = sessionService.findActiveSessionForUser(user);
        if(optionalSession.isPresent()){
            sessionService.delete(optionalSession.get());
        }
        emailService.sendEmail(user.getEmail(),"Ban - Twoje konto zostało zbanowane", "Witaj "+user.getLogin()+", \n Złamałeś" +
                " regulamin strony co poskutkowalo blokadą konta.\n Powód blokady: "+ban.getReason()+"\n\nJeśli nie zgadzasz sie z ta blokadą," +
                " powiadom nas o tym jak najszybciej.");
    }
}
