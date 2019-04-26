package church.clean;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        String smtphost = "securesmtp.t-online.de";
        String from = System.getenv("username");
        String pass = System.getenv("password");

        Properties prop = new Properties();

        prop.put("mail.smtp.host", smtphost);
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");

        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.port", "465");
        prop.put("mail.debug", true);

        prop.put("mail.smtp.connectiontimeout", "10000");
        prop.put("mail.smtp.timeout", "20000");

        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });

        try {
            MimeMessage message = sendMail(from, session);
            Transport.send(message);

            System.out.println("Message send!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static MimeMessage sendMail(String from, Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(from));

        message.setSubject("Please Clean the Church", "UTF-8");

        String msg = "This Message is supposed to remind you to clean the Church";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg,  "text/html;charset=utf-8");

        Multipart multipart = new MimeMultipart();

        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);
        message.setSentDate(new java.util.Date());

        return message;
    }
}
