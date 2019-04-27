package church.clean;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Main {
    public static final String BUILD_NUMBER = "33";

    private static final String SMTPHOST = "securesmtp.t-online.de";
    private static final String PUTZMAIL = "putzdienst-mail@noreply.de";
    private static final String FROM = System.getenv("username");
    private static final String TO = System.getenv("username");
    private static final String PASS = System.getenv("password");

    private static final String FILE = "putzliste.txt";
    public static final String PERSONS = "persons.txt";

    /**
     * "This tool is supposed to send Emails to every person, that is part of the Cleaning-Team this week.
     * @param args
     */
    public static void main(String[] args) {

        System.out.println("Du bist dran mit spülen: #" + BUILD_NUMBER);

        Schedule schedule = generateCleaningList();

        Group nextScheduledGroup = schedule.getNextGroup();
        nextScheduledGroup.print();

//        Session session = createSession();

//        sendEmails(session);
    }
    
    private static Schedule generateCleaningList() {
        System.out.println("We generate the List of cleaners from the File " + FILE + "\n");

        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Schedule schedule = new Schedule();

        BufferedReader reader;
        int linecounter = 0;

        try {
            reader = new BufferedReader(new FileReader(FILE));

            String line;
            while ((line = reader.readLine()) != null) {
                linecounter++;

                String[] segments = line.split(",");

                if(segments.length < 2) {
                    System.out.println("Line " + linecounter + " has to few segments");
                    continue;
                }

                Date appointment = simpleDateFormat.parse(segments[0]);
                Person cleaner = PersonDatabase.getPersonByName(segments[1]);

                if(cleaner == null){
                    continue;
                }

                System.out.println(cleaner.getName() + " is cleaning on: " + simpleDateFormat.format(appointment));
                schedule.addPerson(appointment, cleaner);

            }
            reader.close();
        } catch (FileNotFoundException fnf){
            System.out.println("The File " + FILE + " was not found. Please check if it exists.");
        } catch (IOException e) {
            System.out.println("Something went wrong, while reading the File " + FILE);

            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Invalid Date in line: " + linecounter);
            e.printStackTrace();
        }

        return schedule;
    }

    private static void sendEmails(Session session) {
        try {
            MimeMessage message = generateMessage(TO, session);
            Transport.send(message);

            System.out.println("Message #" + BUILD_NUMBER + " send!");
        } catch (MessagingException e) {
            System.out.println("Message was not send. Please check your firewall.\n"
                    + e.getMessage());
        }
    }

    private static Session createSession() {
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

        return Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASS);
            }
        });
    }

    private static MimeMessage generateMessage(String to, Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(PUTZMAIL));

        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(to));

        message.setSubject("Please Clean the Church #" + BUILD_NUMBER, "UTF-8");

        String msg = "This Message is supposed to remind you to clean the Church. #" +BUILD_NUMBER;

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg,  "text/html;charset=utf-8");

        Multipart multipart = new MimeMultipart();

        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);
        message.setSentDate(new java.util.Date());


        Address[] t = {new InternetAddress(PUTZMAIL)};
        message.setReplyTo(t);

        return message;
    }
}
