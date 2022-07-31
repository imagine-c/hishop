package com.onlineshop.hishop.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendMailThread {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(300);

    public static void sendEmail(JavaMailSender mailSender, MimeMessage mailMessage) {
        executorService.execute(() -> mailSender.send(mailMessage));
    }


    public static void sendEmail(JavaMailSender mailSender, SimpleMailMessage mailMessage) {
        executorService.execute(() -> mailSender.send(mailMessage));
    }
}
