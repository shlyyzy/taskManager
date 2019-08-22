package parsers;

import model.DueDate;
import model.Priority;
import model.Status;
import model.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Represents Task parser
public class TaskParser {

    public static final DueDate NO_DUE_DATE = null;

    // EFFECTS: iterates over every JSONObject in the JSONArray represented by the input
    // string and parses it as a task; each parsed task is added to the list of tasks.
    // Any task that cannot be parsed due to malformed JSON data is not added to the
    // list of tasks.
    // Note: input is a string representation of a JSONArray
    public List<Task> parse(String input) throws RuntimeException {
        List<Task> tasks = new ArrayList<>();
        JSONArray taskArray = new JSONArray(input);

        for (Object object : taskArray) {
            try {
                JSONObject taskJson = (JSONObject) object;

                String description = taskJson.getString("description");
                Task task = new Task(description);

                setTags(taskJson, task);

                setDueDate(taskJson, task);

                setJsonPriority(taskJson, task);

                String statusJson = taskJson.getString("status");
                setJsonStatus(statusJson, task);

                tasks.add(task);
            } catch (RuntimeException e) {
                continue;
            }
        }
        return tasks;
    }

    private static void setTags(JSONObject taskJson, Task task) {
        JSONArray listTags = (JSONArray) taskJson.get("tags");

        for (Object o : listTags) {
            JSONObject object = (JSONObject) o;
            task.addTag(object.getString("name"));
        }
    }

    private static void setDueDate(JSONObject ajson, Task task) {
        if (!(ajson.get("due-date") == JSONObject.NULL)) {
            JSONObject duedateJson;
            duedateJson = (JSONObject) ajson.get("due-date");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MINUTE, (int) duedateJson.get("minute"));
            cal.set(Calendar.HOUR_OF_DAY, (int) duedateJson.get("hour"));
            cal.set(Calendar.DAY_OF_MONTH, (int) duedateJson.get("day"));
            cal.set(Calendar.MONTH, (int) duedateJson.get("month"));
            cal.set(Calendar.YEAR, (int) duedateJson.get("year"));
            DueDate dueDate = new DueDate(cal.getTime());
            task.setDueDate(dueDate);
        }
    }

    private static void setJsonPriority(JSONObject taskJson, Task task) {
        JSONObject priorityJson = (JSONObject) taskJson.get("priority");

        Priority p = new Priority();

        Object jsonimportant = priorityJson.get("important");


        boolean important = priorityJson.getBoolean("important");
        boolean urgent = priorityJson.getBoolean("urgent");
        p.setImportant(important);
        p.setUrgent(urgent);
        task.setPriority(p);
    }

    private static void setJsonStatus(String statusJson, Task task) {
        Status status = null;

        if (statusJson.equals("IN_PROGRESS")) {
            status = Status.IN_PROGRESS;
        } else if (statusJson.equals("UP_NEXT")) {
            status = Status.UP_NEXT;
        } else if (statusJson.equals("TODO")) {
            status = Status.TODO;
        } else if (statusJson.equals("DONE")) {
            status = Status.DONE;
        }

        task.setStatus(status);
    }

}
