package com.ixpert.isampleapp1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    @Async
    public void sendMail(SimpleMailMessage email){
        mailSender.send(email);
    }

}
