package com.lhind.annualleave.services.email.builder;

public final class EmailInfo {

    // Encapsulation
    private String from;
    private String to;
    private String subject;
    private String content;
    private String bcc;
    private String cc;

    // empty constructor, self-generated , package private , default access modifier
    //prevents direct creation from constructor, uses fluentbuilder
    EmailInfo() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBCC() {
        return bcc;
    }

    public void setBCC(String bcc) {
        this.bcc = bcc;
    }

    public String getCC() {
        return cc;
    }

    public void setCC(String cc) {
        this.cc = cc;
    }
}
