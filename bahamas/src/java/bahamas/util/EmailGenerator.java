/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.util;

import java.util.Properties;
import javax.mail.Address;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailGenerator {

    private static final String USERNAME = "mailagent@twc2.org.sg";
    private static final String PASSWORD = "0kS3%6su31RR";

    /*
    public static void main(String[] args){
        String[] temp ={"Huxley","haha","123"};
        System.out.println(sendEmail("huxley.goh.2014@sis.smu.edu.sg",temp));
    }
    */
    /**
     * <p>
     * Send email to contacts
     * </p>
     *
     * @param email the email address to send email to
     * @param args 1.Name,2.username,3.password
     * @return boolean value, true if email is send successfully
     */
    public static boolean sendEmail(String email, String[] args) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("TWC2 Bahamas - Login Details");
            message.setText("Dear " + args[0] + ","
                    + "\n\n Thank you for signing up! You may now log in at"
                    + " https://rms.twc2.org.sg/bahamas using the information below:"
                    + "\n\n Username: " + args[1] + "\n Password: " + args[2] 
                    + "\n\n You may also change your password once you've logged in." 
                    + "\n\n Regards," + "\n TWC2 Team");

            Transport.send(message);
            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    
    public static boolean verifyEmail(String email, String name, String hashID) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("TWC2 Bahamas - Verify tyour email address");
            message.setText("Dear " + name + ","
                    + "\n\n Your email address has not been verified."
                    + " Verify your email address: http://localhost:8084/bahamas/verifyemail?verifyemail=" + hashID
                    + "\n\n Regards," + "\n TWC2 Team");

            Transport.send(message);
            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
