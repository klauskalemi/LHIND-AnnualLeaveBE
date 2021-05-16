package com.lhind.annualleave.http.controllers;

import com.lhind.annualleave.http.security.JwtTokenUtils;
import com.lhind.annualleave.persistence.models.LeaveRequest;
import com.lhind.annualleave.persistence.models.User;
import com.lhind.annualleave.services.LeaveRequestService;
import com.lhind.annualleave.services.UserService;
import com.lhind.annualleave.services.email.EmailService;
import com.lhind.annualleave.services.email.builder.EmailFluentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    public LeaveRequestController(LeaveRequestService leaveRequestService,
                                  EmailService emailService,
                                  UserService userService) {
        this.leaveRequestService = leaveRequestService;
        this.emailService = emailService;
        this.userService = userService;
    }

    @ResponseBody
    @GetMapping("/leave-requests")
    public ResponseEntity<Iterable<LeaveRequest>> findAll() {
        return new ResponseEntity<>(leaveRequestService.findAll(), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/leave-requests")
    public ResponseEntity<LeaveRequest> save(@RequestBody LeaveRequest leaveRequest) {
        return new ResponseEntity<>(leaveRequestService.save(leaveRequest), HttpStatus.CREATED);
    }

    @ResponseBody
    @PutMapping("/leave-requests")
    public ResponseEntity<LeaveRequest> update(@RequestBody LeaveRequest leaveRequest,
                                               @RequestHeader("Authorization") String authorizationHeader) {
        Optional<LeaveRequest> requestFromDB = leaveRequestService.findById(leaveRequest.getId());
        if (requestFromDB.isPresent() && requestFromDB.get().isRequestWaiting()
                && (leaveRequest.isRequestAccepted() && leaveRequest.isRequestRejected())) {
            sendEmailForStatusChange(leaveRequest, authorizationHeader);
        }
        return new ResponseEntity<>(leaveRequestService.update(leaveRequest), HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping("/leave-requests/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        return leaveRequestService.delete(id)
                ? new ResponseEntity<>("", HttpStatus.OK)
                : new ResponseEntity<>("", HttpStatus.CONFLICT);
    }

    private void sendEmailForStatusChange(LeaveRequest leaveRequest, String authorizationHeader) {
        String jwtToken = authorizationHeader.replace(JwtTokenUtils.PREFIX, "");
        String usernameFromToken = JwtTokenUtils.getUsernameFromToken(jwtToken);
        // Repository returns Optional
        // user Optional Wrapper in order to avoid getting null with all its exceptions
        Optional<User> userOptional = userService.findUserByUsername(usernameFromToken);

        try {
            userOptional.ifPresent(user -> emailService.sendEmail(EmailFluentBuilder.getNewInstance()
                .setFrom(emailService.getEmailAccountOfSystem())
                .setTo(user.getEmail())
                .setSubject(EmailService.LEAVE_STATUS_CHANGE_SUBJECT)
                .setContent(leaveRequest.isRequestAccepted() ? "Request Accepted!" : "Request Rejected!")
                .build()));
        } catch (Exception ignored) {}
    }

}
