package com.lhind.annualleave.services;

import com.lhind.annualleave.persistence.models.InvalidToken;
import com.lhind.annualleave.persistence.models.User;
import com.lhind.annualleave.persistence.repositories.IInvalidTokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class InvalidTokenService {

    private final IInvalidTokensRepository iInvalidTokensRepository;

    @Autowired
    public InvalidTokenService(IInvalidTokensRepository iInvalidTokensRepository) {
        this.iInvalidTokensRepository = iInvalidTokensRepository;
    }

    public boolean isTokenStoredAsInvalid(String token) {
        Optional<InvalidToken> invalidTokenOptional = iInvalidTokensRepository.findByToken(token);
        return invalidTokenOptional.isPresent();
    }

    public InvalidToken save(InvalidToken invalidToken) {
        return iInvalidTokensRepository.save(invalidToken);
    }

    public void deleteTokensOfUser(User user) {
        iInvalidTokensRepository.deleteAllByUserBeforeDate(user.getId(), new Date());
    }
}
