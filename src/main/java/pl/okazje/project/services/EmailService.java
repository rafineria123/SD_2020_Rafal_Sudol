package pl.okazje.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body){
        Thread t = new Thread(() -> {
            try {
                MimeMessage message=javaMailSender.createMimeMessage();
                MimeMessageHelper helper;
                helper=new MimeMessageHelper(message,true);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(body);
                javaMailSender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }}