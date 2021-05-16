package com.lhind.annualleave.services.email.builder;

public class EmailFluentBuilder implements IEmailFrom, IEmailTo, IEmailSubject, IEmailContent, IEmailBuilder {

    private final EmailInfo emailInfo;

    /**
     * Private constructor to prevent object creation
     */
    private EmailFluentBuilder() {
        emailInfo = new EmailInfo();
    }

    /**
     * Getting the instance method
     * @return A new instance of the fluent builder
     */
    public static IEmailFrom getNewInstance(){
        return new EmailFluentBuilder();
    }
    
    @Override
    public IEmailTo setFrom(String from) {
        emailInfo.setFrom(from);
        return this;
    }

    @Override
    public IEmailSubject setTo(String to) {
        emailInfo.setTo(to);
        return this;
    }

    @Override
    public IEmailContent setSubject(String subject) {
        emailInfo.setSubject(subject);
        return this;
    }

    @Override
    public IEmailBuilder setContent(String content) {
        emailInfo.setContent(content);
        return this;
    }

    @Override
    public IEmailBuilder setBCC(String bcc) {
        emailInfo.setBCC(bcc);
        return this;
    }

    @Override
    public IEmailBuilder setCC(String cc) {
        emailInfo.setCC(cc);
        return this;
    }

    @Override
    public EmailInfo build() {
        return emailInfo;
    }
}
