package church.clean;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;


class PersonDatabase {

    private HashSet<Person> allPersons;

    private static PersonDatabase instance = new PersonDatabase();
    private PersonDatabase(){
        allPersons = new HashSet<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(Main.PERSONS));

            //skip the first line
            reader.readLine();
            int linecounter = 2;

            String line = reader.readLine();
            while (line != null) {
//                System.out.println(line);

                String[] segments = line.split(";");

                if(segments.length < 3) {
                    System.out.println("Line " + linecounter + " has to few segments");
                    continue;
                }

                Person x = new Person(segments[0], segments[1], segments[2]);
                allPersons.add(x);

                // read next line
                line = reader.readLine();
                linecounter++;
            }
            reader.close();
        } catch (FileNotFoundException fnf){
            System.out.println("The File " + Main.PERSONS + " was not found. Please check if it exists.");
        } catch (IOException e) {
            System.out.println("Something went wrong, while reading the File " + Main.PERSONS);
        }

    }

    static HashSet<Person> getAllPersons() {
        return instance.allPersons;
    }

    @Nullable
    static Person getPersonByName(String name) {
        for (Person person : getAllPersons()) {
            if(person.hasName(name))
                return person;
        }

        System.err.println("Person with name " + name + " not found!");
        return null;

    }

    static Person getPersonByName(String firstname, String surname) {
        return getPersonByName(firstname + " " + surname);
    }

}
