package church.clean;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Schedule {

    private Map<Date, Group> schedule = new HashMap<>();

    public Schedule(String file){
        System.out.println("We generate the List of cleaners from the File " + file + "\n");

        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        BufferedReader reader;
        int linecounter = 0;

        try {
            reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                linecounter++;

                String[] segments = line.split(";");

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
                addPerson(appointment, cleaner);

            }
            reader.close();
        } catch (FileNotFoundException fnf){
            System.out.println("The File " + file + " was not found. Please check if it exists.");
        } catch (IOException e) {
            System.out.println("Something went wrong, while reading the File " + file);

            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Invalid Date in line: " + linecounter);
            e.printStackTrace();
        }
    }

    private void addPerson(Date date, Person person){

        if(!schedule.keySet().contains(date)){
            schedule.put(date, new Group());
        }

        Group group = schedule.get(date);
        group.add(person);
    }

    Group getGroup(Date date){
        Group group = schedule.get(date);

        if (group == null){
            System.err.println("No group scheduled for " + date);
            group = new Group();
        }

        return group;
    }

    Group getNextGroup(){
        Group group = null;

        String pattern = "dd.MM.yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date today = new Date();

        Date closest = null;
        long dueNext = Long.MAX_VALUE;
        for (Date scheduledDate : schedule.keySet()) {
            long dueIn = scheduledDate.getTime() - today.getTime();

            if(dueIn < dueNext && dueIn >= 0){
                closest = scheduledDate;
                dueNext = dueIn;
            }
        }

        if(closest != null)
            return schedule.get(closest);
        else
            return new Group();
    }
}
