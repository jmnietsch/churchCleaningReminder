package church.clean;

import java.util.*;

public class Group extends HashSet<Person> {
    public static int GROUPSIZE = 5;

    public Group(){
        this(GROUPSIZE);
    }

    public Group(int size){
        super(size);
    }

    public void prettyPrint() {
        System.out.println("This Group contains:");
        this.print();
        System.out.println("\n");
    }

    public void print() {
        System.out.println(toPrettyString());
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append("[");
        for (Person person : toSortedList()) {
            s.append(person.getName());
            s.append(",");
        }
        s.append("]");

        return s.toString();
    }

    public List<Person> toSortedList(){
        List<Person> sortedPersons = new ArrayList<>(this);
        Collections.sort(sortedPersons);

        return sortedPersons;
    }

    public String toPrettyString(){
        StringBuilder string = new StringBuilder();

        for (Person person : toSortedList()) {
            string.append(person.toString())
                    .append("\r\n");
        }

        return string.toString();
    }
}
