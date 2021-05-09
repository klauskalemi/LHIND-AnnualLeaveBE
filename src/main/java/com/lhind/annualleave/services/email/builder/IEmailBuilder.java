package com.lhind.annualleave.services.email.builder;

public interface IEmailBuilder {
    IEmailBuilder setBCC(String bcc);
    IEmailBuilder setCC(String cc);
    EmailInfo build();
}
