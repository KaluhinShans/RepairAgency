package com.shans.kaluhin.service;

import com.shans.kaluhin.ProjectProperties;
import com.shans.kaluhin.entity.Order;
import com.shans.kaluhin.entity.User;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailSenderService {
    private static final Logger log = Logger.getLogger(MailSenderService.class);
    private static String name = ProjectProperties.getProperty("mail.username");
    private static String password = ProjectProperties.getProperty("mail.password");

    public static void sendActivationCode(User user){
        String message = String.format("Hello, %s \n" +
                "Welcome to Repair Agency, Please visit next link to activation your email: " +
                ProjectProperties.getProperty("host") + "/activate/?code=%s", user.getFullName(), user.getActivationCode());

        MailSenderService.send(user.getEmail(), "Activation code", message);
        log.info("Activation code send");
    }

    public static void sendOrderStatusUpdate(User user, Order order){
        String message = String.format("Hello, %s \n" +
                "Your order received status '%s' \n" +
                        "Follow link to se more: " +
                ProjectProperties.getProperty("host") + "/orders", user.getFullName(), order.getStatus());

        MailSenderService.send(user.getEmail(), "New Order status", message);
        log.info("Status update send to user");
    }

    public static void sendOrderPayment(User user, Order order){
        String message = String.format("Hello, %s \n" +
                "You paid %d$ for the order №: %d '%s'\n" +
                "please wait while the master accepts your order \n" +
                "Follow link to se more: " +
                ProjectProperties.getProperty("host") + "/orders", user.getFullName(), order.getPrice(), order.getId(), order.getName());

        MailSenderService.send(user.getEmail(), "New Order status", message);
        log.info("Order payment information send");
    }

    public static void sendTopUpBalance(User user, int sum){
        String message = String.format("Hello, %s \n" +
                "Your balance has been replenished with %d $ \n" +
                "Your balance: %d $ \n" +
                "Have a nice renovation \n" +
                "Follow link to se more: " +
                ProjectProperties.getProperty("host") + "/profile", user.getFullName(), sum, user.getBalance());

        MailSenderService.send(user.getEmail(), "New Order status", message);
        log.info("Balance replenish information send to user");
    }

    public static void sendWithdrawBalance(User user, int sum, String card){
        String message = String.format("Hello, %s \n" +
                "Your balance has been charged %d $ for this card '%s' \n" +
                "Your balance: %d $ \n" +
                "Have a nice renovation \n" +
                "Follow link to se more: " +
                ProjectProperties.getProperty("host") + "/profile", user.getFullName(), sum, card, user.getBalance());

        MailSenderService.send(user.getEmail(), "New Order status", message);
        log.info("Cash out the balance information send to user");
    }

   public static void send(String toEmail, String subject, String text){
       Session session = Session.getInstance(ProjectProperties.gerEmailProperties(), new Authenticator() {
           protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(name, password);
           }
       });
       try {
           Message message = new MimeMessage(session);
           message.setFrom(new InternetAddress(name));
           message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
           message.setSubject(subject);
           message.setText(text);

           Transport.send(message);
       } catch (MessagingException e) {
           log.error("Email message don't send");
           throw new RuntimeException(e);
       }
   }

}
