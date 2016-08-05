/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bahamas.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailGenerator {

    private static final String PROPS_FILENAME = "/email.properties";
    private static String USERNAME = "";
    private static String PASSWORD = "";
    private static String LOCATION = "";

    public static void init() {
        try {
            InputStream is = Authenticator.class.getResourceAsStream(PROPS_FILENAME);
            Properties props = new Properties();
            props.load(is);

            USERNAME = props.getProperty("email.username");
            PASSWORD = props.getProperty("email.password");
            LOCATION = props.getProperty("email.location");

        } catch (Exception ex) {
            String message = "Unable to load '" + PROPS_FILENAME + "'.";

            System.out.println(message);
            Logger.getLogger(Authenticator.class.getName()).log(Level.SEVERE, message, ex);
            throw new RuntimeException(message, ex);

        }
    }

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
        init();
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
        init();
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
            message.setSubject("TWC2 Bahamas - Email Verification");
            message.setText("Dear " + name + ","
                    + "\n\nYour email address has not been verified."
                    + "\nClick this link to complete the verification: " + LOCATION + "/bahamas/verifyemail?verifyemail=" + hashID
                    + "\n\nRegards," + "\n TWC2 Team");

            Transport.send(message);
            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
