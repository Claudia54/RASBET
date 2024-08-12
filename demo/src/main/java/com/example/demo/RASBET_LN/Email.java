package com.example.demo.RASBET_LN;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {
   private String mail;
   private String subject;
   private String body;
   private String pwd;

   public Email(String mail, String subject, String body){
      this.mail=mail;
      this.subject=subject;
      this.body=body;
      Properties p = new Properties();
      try (FileReader in = new FileReader("src/main/java/com/example/demo/RASBET_DATA/pwd.properties")) {
         p.load(in);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      this.pwd=p.getProperty("pwd");
   }

   /**
    * Function that generates and sends the Email to inform a user he won a bet
    * @param email String
    */
   public static void generateEmailBetWon(String email){
      String subject = "RASBET - Aposta Ganha " + new String(Character.toChars(0x1F4B8));;
      String body = "PARABÉNS! \nUma das tuas apostas estava correta. \nConsulta agora os teus ganhos.";
      Email e = new Email(email,subject,body);
      e.sendEmail();
   }

   /**
    * Function that generates and sends the Email inform a user that a game he is following changes odds or state
    * @param email String
    * @param game String
    */
   public static void generateEmailFollowers(String email, String game){
      String subject = "RASBET - Alerta "+game+ " "; //+ new String(Character.toChars(0x1F4B8));;
      String body = "Houve uma alteraçao no jogo "+game+" que está a seguir!!";
      Email e = new Email(email,subject,body);
      e.sendEmail();
   }

   /**
    * Function used to send an email
    */
   public void sendEmail() {

      // Sender's email ID needs to be mentioned
      String from = "rasbet.ras2223@gmail.com";

      // Assuming you are sending email from through gmails smtp
      String host = "smtp.gmail.com";

      // Get system properties
      Properties properties = System.getProperties();

      // Setup mail server
      properties.put("mail.smtp.host", host);
      properties.put("mail.smtp.port", "465");
      properties.put("mail.smtp.ssl.enable", "true");
      properties.put("mail.smtp.auth", "true");

      String pwd = this.pwd;

      // Get the Session object.// and pass username and password
      Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

         protected PasswordAuthentication getPasswordAuthentication() {

            return new PasswordAuthentication("rasbet.ras2223@gmail.com", pwd);

         }

      });

      try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.This is the Subject Line!
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.mail));

         // Set Subject: header field
         message.setSubject(this.subject);

         // Now set the actual message
         message.setText(this.body);

         System.out.println("sending...");
         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
      } catch (MessagingException mex) {
         mex.printStackTrace();
      }

   }

}