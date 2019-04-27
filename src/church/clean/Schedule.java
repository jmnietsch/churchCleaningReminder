package church.clean;

import java.text.SimpleDateFormat;
import java.util.*;

public class Schedule {

    Map<Date, Group> schedule = new HashMap<>();

    void addPerson(Date date, Person person){

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
