package church.clean;

import java.util.HashSet;

public class Group extends HashSet<Person> {
    public static int GROUPSIZE = 5;

    public Group(){
        this(GROUPSIZE);
    }

    public Group(int size){
        super(size);
    }

    public void print() {
        System.out.println("This Group contains:\n");
        for (Person person : this) {
            System.out.println(person.toString());
        }
        System.out.println("\n");
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
