package com.lhind.annualleave.services.email;

import com.lhind.annualleave.services.email.builder.EmailInfo;
import org.springframework.mail.MailException;

public interface IEmailService {
    void sendEmail(EmailInfo emailInfo) throws MailException;
}
