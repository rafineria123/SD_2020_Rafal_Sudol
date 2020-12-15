package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Token;
import pl.okazje.project.entities.User;
import pl.okazje.project.repositories.TokenRepository;
import pl.okazje.project.repositories.UserRepository;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository  tokenRepository;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        final User optionalUser = userRepository.findUserByLogin(s);

        if (optionalUser!=null) {
            return optionalUser;
        } else {
            throw new UsernameNotFoundException(MessageFormat.format("User with login {0} cannot be found.", s));

        }
    }


    public User getUser(String verificationToken) {
        User user = tokenRepository.findByToken(verificationToken).getUser();
        return user;
    }

    public Token getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }


    public void createVerificationToken(User user, String token) {
        Token myToken = new Token();
        myToken.setToken(token);
        myToken.setUser(user);
        tokenRepository.save(myToken);
        user.setToken(myToken);
        userRepository.save(user);
    }


}
