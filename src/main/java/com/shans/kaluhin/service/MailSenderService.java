package com.shans.kaluhin.service;

import com.shans.kaluhin.ProjectProperties;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailSenderService {
    private static String name = ProjectProperties.getProperty("mail.username");
    private static String password = ProjectProperties.getProperty("mail.password");

   public static void send(String toEmail, String subject, String text){
       Session session = Session.getInstance(ProjectProperties.gerEmailProperties(), new Authenticator() {
           protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(name, password);
           }
       });
       try {
           Message message = new MimeMessage(session);
           //от кого
           message.setFrom(new InternetAddress(name));
           //кому
           message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
           //Заголовок письма
           message.setSubject(subject);
           //Содержимое
           message.setText(text);

           //Отправляем сообщение
           Transport.send(message);
       } catch (MessagingException e) {
           throw new RuntimeException(e);
       }
   }

}
