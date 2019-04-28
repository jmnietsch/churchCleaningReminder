package church.clean;

import org.jetbrains.annotations.NotNull;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static church.clean.Main.BUILD_NUMBER;

public class Person implements Comparable<Person>{
    private String mailadress;
    private String firstname;
    private String surname;

    public Person(String firstname, String surname, String mailadress) {
        this.firstname = firstname.replaceAll("\"", "");
        this.surname = surname.replaceAll("\"", "");
        this.mailadress = mailadress.replaceAll("\"", "");
    }

    public String getMailadressString() {
        return mailadress;
    }

    public InternetAddress getMailadress() throws AddressException {
        return new InternetAddress(getMailadressString());
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public boolean hasMailAdress(){
        if(mailadress.length() == 0)
            return false;

        try {
            getMailadress();
        } catch (AddressException e) {
            return false;
        }
        return true;

    }

    public String getName() {
        return getFirstname() + " " + getSurname();
    }

    @Override
    public String toString() {
        String mailAddressString = " (" + getMailadressString() + ")";
        return getName()
                + (hasMailAdress() ? mailAddressString : "");
    }

    public boolean hasName(String firstname, String surname) {
        return (this.firstname.equals(firstname)) && (this.surname.equals(surname));
    }

    public boolean hasName(String name) {
        return this.getName().equals(name);
    }

    @Override
    public int compareTo(@NotNull Person o) {
        int surnameCompare = this.getSurname().compareTo(o.getSurname());

        if (surnameCompare != 0)
            return surnameCompare;
        else
            return this.getFirstname().compareTo(o.getFirstname());

    }

    public void sendMail(MimeMessage message) {
        if(message == null){
            System.err.println("Skipped sending empty message to " + getName());
            return;
        }

        if(!hasMailAdress()){
            System.err.println("You cannot send an Email to " + getName() + ". There is no Mail-address available.");
            return;
        }

        try {
            Transport.send(message);

            System.out.println("Message #" + BUILD_NUMBER + " send to " + this.getName() + "!");
        } catch (MessagingException e) {
            System.err.println("Message was not send. Please check your firewall.");
            System.err.println(e.getMessage());
        }

    }
}
