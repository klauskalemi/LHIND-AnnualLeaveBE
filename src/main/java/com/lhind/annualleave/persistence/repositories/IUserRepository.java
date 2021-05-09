package com.lhind.annualleave.persistence.repositories;

import com.lhind.annualleave.persistence.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User AS u WHERE lower(u.username) = lower(:username)")
    Optional<User> findByUsername(String username);
}
