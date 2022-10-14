package com.ab.worldcup.email;

public interface EmailSender {
    void send(String to, String subject, String email);
}
