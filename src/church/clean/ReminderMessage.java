package church.clean;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;

import static church.clean.Main.BUILD_NUMBER;
import static church.clean.Main.PUTZMAIL;

public class ReminderMessage extends MimeMessage {

    public ReminderMessage(Session session, Person person, Date date, Group team){
        super(session);

        try {
            setFrom(new InternetAddress(PUTZMAIL));

            setRecipients(
                    Message.RecipientType.TO, person.getMailadressString());

            setSubject("Dear " + person.getFirstname()
                    + ": Please Clean the Church on " + Schedule.simpleDateFormat.format(date)
                    + " #" + BUILD_NUMBER, "UTF-8");

            StringBuilder msg = new StringBuilder();
            msg.append("Dear ")
                    .append(person.getFirstname())
                    .append(",\n")

                    .append("This ReminderMessage is supposed to remind you to clean the Church. #" +BUILD_NUMBER + "\n\n")

                    .append("You are part of the Team, that will clean our Church on ")
                    .append(Schedule.simpleDateFormat.format(date))
                    .append("\n")

                    .append("Your Team consists of: ")
                    .append(team.toPrettyString());

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg.toString(),  "text/html;charset=utf-8");

            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(mimeBodyPart);

            setContent(multipart);
            setSentDate(new java.util.Date());


            Address[] t = {new InternetAddress(PUTZMAIL)};
            setReplyTo(t);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
