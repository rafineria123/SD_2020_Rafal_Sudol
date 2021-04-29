package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Token;
import pl.okazje.project.repositories.TokenRepository;

import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Optional<Token> findByToken(String token){
        return Optional.of(tokenRepository.findFirstByToken(token));
    }

    public void save(Token token){
        tokenRepository.save(token);
    }

}
