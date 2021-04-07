package pl.okazje.project.services;

import org.springframework.stereotype.Service;
import pl.okazje.project.entities.Token;
import pl.okazje.project.repositories.TokenRepository;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token findByToken(String token){

        return tokenRepository.findFirstByToken(token);

    }

}
