package com.lhind.annualleave.persistence.repositories;

import com.lhind.annualleave.persistence.models.InvalidToken;
import com.lhind.annualleave.persistence.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

public interface IInvalidTokensRepository extends JpaRepository<InvalidToken, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM InvalidToken it WHERE it.user.id = :userID AND it.expiration <= :date")
    void deleteAllByUserBeforeDate(@Param("userID") long userID, @Param("date") Date date);

    @Query("SELECT it FROM InvalidToken AS it WHERE it.token = :token")
    Optional<InvalidToken> findByToken(String token);
}
