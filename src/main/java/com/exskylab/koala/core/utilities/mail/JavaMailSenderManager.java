package com.exskylab.koala.core.utilities.mail;

import com.exskylab.koala.core.constants.messages.EmailMessages;
import com.exskylab.koala.core.exceptions.EmailSendException;
import com.exskylab.koala.core.utilities.mail.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class JavaMailSenderManager implements EmailService {

    private final JavaMailSender javaMailsender;

    private static final Logger logger = LoggerFactory.getLogger(JavaMailSenderManager.class);

    public JavaMailSenderManager(JavaMailSender javaMailsender) {
        this.javaMailsender = javaMailsender;
    }

    @Value("${spring.mail.username}")
    private String from;


    @Override
    public boolean sendMail(String to, String subject, String body) {

        MimeMessage mimeMessage = javaMailsender.createMimeMessage();

        try{
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            javaMailsender.send(mimeMessage);
            return true;
        }catch (Exception e){
            logger.error("E-posta gönderimi başarısız oldu: {} - Hata: {}", to, e.getMessage(), e);
            throw new EmailSendException(EmailMessages.EMAIL_SEND_FAILED, e);
        }


    }
}