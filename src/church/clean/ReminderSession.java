package church.clean;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

import static church.clean.Main.*;

public class ReminderSession {
    private Session sessionInstance;

    ReminderSession() {
        Properties prop = new Properties();

        prop.put("mail.smtp.host", SMTPHOST);
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");

        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.port", "465");
        prop.put("mail.debug", true);

        prop.put("mail.smtp.connectiontimeout", "10000");
        prop.put("mail.smtp.timeout", "20000");

        sessionInstance = Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASS);
            }
        });
    }

    public Session getSession() {
        return sessionInstance;
    }
}
