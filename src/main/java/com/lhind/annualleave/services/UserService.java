package com.lhind.annualleave.services;

import com.lhind.annualleave.persistence.models.User;
import com.lhind.annualleave.persistence.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final IUserRepository iUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(IUserRepository iUserRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.iUserRepository = iUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<User> findAll() {
        return iUserRepository.findAll();
    }

    public Optional<User> findUserByUsername(String username) {
        return iUserRepository.findByUsername(username);
    }

    public Optional<User> findUserByID(long id) {
        return iUserRepository.findById(id);
    }

    public User save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDate.now());
        return iUserRepository.save(user);
    }

    public User update(User user) {
        return iUserRepository.save(user);
    }

    public boolean delete(long id) {
        Optional<User> userOptional = iUserRepository.findById(id);
        if (!userOptional.isPresent()) {
            return false;
        }
        iUserRepository.delete(userOptional.get());

        return true;
    }

}
