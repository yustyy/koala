package com.exskylab.koala.core.utilities.mail;

public interface EmailService {

    boolean sendMail(String to, String subject, String body);

}