package church.clean;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Person {
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
        return getName() + "(" + getMailadressString() + ")";
    }

    public boolean hasName(String firstname, String surname) {
        return (this.firstname.equals(firstname)) && (this.surname.equals(surname));
    }

    public boolean hasName(String name) {
        return this.getName().equals(name);
    }
}
