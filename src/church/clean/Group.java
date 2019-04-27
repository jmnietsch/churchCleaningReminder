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
        List<Person> sortedPersons = new ArrayList<>(this);
        Collections.sort(sortedPersons);

        for (Person person : sortedPersons) {
            System.out.println(person.toString());
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append("[");
        for (Person person : this) {
            s.append(person.getName());
            s.append(",");
        }
        s.append("]");

        return s.toString();
    }
}
