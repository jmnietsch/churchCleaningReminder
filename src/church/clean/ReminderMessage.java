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
    private final Date date;
    private final Group members;
    private Group analogMembers;

    public ReminderMessage(Session session, Date date, Group team){
        super(session);
        this.date = date;
        members = team;

        analogMembers = new Group();

        for (Person member : members)
            if(!member.hasMailAdress())
                analogMembers.add(member);

        try {
            setFrom(new InternetAddress(PUTZMAIL));
            setSubject("Empty Default Subject");

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent("Empty Default Message",  "text/plain;charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            setContent(multipart);

            Address[] t = {new InternetAddress(PUTZMAIL)};
            setReplyTo(t);

            setSentDate(new java.util.Date());

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public MimeMessage personaliseMessageFor(Person person) {

        try {
            MimeMessage personalisedMessage = new MimeMessage(this);
            StringBuilder contentBuilder = new StringBuilder();

            personalisedMessage.setSubject(getSubjectFor(person), "UTF-8");
            personalisedMessage.setRecipients(Message.RecipientType.TO, person.getMailadressString());

            contentBuilder
                    .append(getBasicReminderTextFor(person))
                    .append(getTeamMemberText());

            if(analogMembers.size() != 0)
                contentBuilder.append(getAnalogReminderText());

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(contentBuilder.toString(),  "text/plain;charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            personalisedMessage.setContent(multipart);

            return personalisedMessage;
        } catch (MessagingException e) {
            e.printStackTrace();

            System.err.println("No personalised Message was created! Uses default Message instead.");
        }

        return this;
    }

    private String getSubjectFor(Person person){
        return "Dear " + person.getFirstname() + ": "
                + "Please Clean the Church on " + Schedule.simpleDateFormat.format(this.date)
                + " #" + BUILD_NUMBER;
    }

    private String getBasicReminderTextFor(Person person){
        return "Dear " +
                person.getFirstname() +
                ",\n" +
                "This Message is supposed to remind you to clean the Church. #" + BUILD_NUMBER + "\n\n" +
                "You are part of the Team, that will clean our Church on " +
                Schedule.simpleDateFormat.format(date);
    }

    private String getTeamMemberText() {
        return "\n\nYour Team consists of:\n"
                + members.toPrettyString();
    }

    private String getAnalogReminderText() {
        return "\n\nSince part of your Team has no E-Mail Addresses, please make sure, to remind them of your appointment.\n" +
                "The following Members still need to be informed:\n" +
                analogMembers.toPrettyString();
    }
}
