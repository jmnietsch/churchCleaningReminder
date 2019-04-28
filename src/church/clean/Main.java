package church.clean;

import java.util.*;

public class Main {
    public static final String BUILD_NUMBER = "50";

    public static final String SMTPHOST = "securesmtp.t-online.de";
    public static final String PUTZMAIL = "putzdienst-mail@noreply.de";
    public static final String FROM = System.getenv("username");
    public static final String PASS = System.getenv("password");

    public static final String SCHEDULE = "random_clening_schedule.CSV";
    public static final String PERSONS = "random_persons.txt";

    /**
     * "This tool is supposed to send Emails to every person, that is part of the Cleaning-Team this week.
     * @param args
     */
    public static void main(String[] args) {

        System.out.println("Du bist dran mit spülen: #" + BUILD_NUMBER);

        Schedule schedule = new Schedule(SCHEDULE, new PersonDatabase(PERSONS));

        Group nextScheduledGroup = schedule.getNextGroup();
        Date nextScheduledDate = schedule.getNextDate();

        if(nextScheduledDate == null){
            System.err.println("No Team Scheduled. Abort Programm");
            System.exit(-1);
        }

        ReminderSession session = new ReminderSession();
        ReminderMessage message = new ReminderMessage(session.getSession(), nextScheduledDate, nextScheduledGroup);

        for (Person person : nextScheduledGroup){
            person.sendMail(message.personaliseMessageFor(person));
        }
    }
}
