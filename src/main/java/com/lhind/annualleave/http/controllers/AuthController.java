package com.lhind.annualleave.http.controllers;

import com.lhind.annualleave.http.security.JwtTokenUtils;
import com.lhind.annualleave.persistence.models.InvalidToken;
import com.lhind.annualleave.persistence.models.User;
import com.lhind.annualleave.services.InvalidTokenService;
import com.lhind.annualleave.services.UserService;
import com.lhind.annualleave.services.email.EmailService;
import com.lhind.annualleave.services.email.builder.EmailFluentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
public class AuthController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final InvalidTokenService invalidTokenService;
    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public AuthController(BCryptPasswordEncoder bCryptPasswordEncoder,
                          InvalidTokenService invalidTokenService,
                          UserService userService,
                          EmailService emailService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.invalidTokenService = invalidTokenService;
        this.emailService = emailService;
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace(JwtTokenUtils.PREFIX, "");
        String usernameFromToken = JwtTokenUtils.getUsernameFromToken(jwtToken);
        Optional<User> userOptional = userService.findUserByUsername(usernameFromToken);

        if (userOptional.isPresent()) {
            Date expiration = JwtTokenUtils.getExpirationDateFromToken(jwtToken);

            InvalidToken invalidToken = new InvalidToken();
            invalidToken.setUser(userOptional.get());
            invalidToken.setToken(jwtToken);
            invalidToken.setExpiration(expiration);
            invalidTokenService.save(invalidToken);
        }

        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/auth/change-password")
    public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String authorizationHeader,
                                                 @RequestBody String password) {
        Optional<User> userOptional = getTokenFromRequestHeader(authorizationHeader);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Cannot find user to update");
        }

        User user = userOptional.get();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userService.update(user);

        sendPasswordChangeEmail(user);

        return new ResponseEntity<>("", HttpStatus.OK);
    }

    private Optional<User> getTokenFromRequestHeader(String authorizationHeader) {
        String jwtToken = authorizationHeader.replace(JwtTokenUtils.PREFIX, "");
        String usernameFromToken = JwtTokenUtils.getUsernameFromToken(jwtToken);
        return userService.findUserByUsername(usernameFromToken);
    }

    private void sendPasswordChangeEmail(User user) {
        try {
            emailService.sendEmail(EmailFluentBuilder.getNewInstance()
                    .setFrom(emailService.getEmailAccountOfSystem())
                    .setTo(user.getEmail())
                    .setSubject(EmailService.CHANGE_PASSWORD_SUBJECT)
                    .setContent("Password changed for user " + user.getUsername())
                    .build());
        } catch (Exception ignored) {}
    }

}
