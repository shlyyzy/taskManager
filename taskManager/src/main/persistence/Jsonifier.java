package persistence;


import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

// Converts model elements to JSON objects
public class Jsonifier {

    // EFFECTS: returns JSON representation of tag
    public static JSONObject tagToJson(Tag tag) {
        JSONObject tagJson = new JSONObject();

        tagJson.put("name", noHashTagTag(tag));

        return tagJson;
    }

    // EFFECTS: returns JSON representation of priority
    public static JSONObject priorityToJson(Priority priority) {
        JSONObject priorityJson = new JSONObject();
        priorityJson.put("important", priority.isImportant());
        priorityJson.put("urgent", priority.isUrgent());

        return priorityJson;
    }

    // EFFECTS: returns JSON respresentation of dueDate
    public static JSONObject dueDateToJson(DueDate dueDate) {
        JSONObject dueDateJson = new JSONObject();

        if (dueDate == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        Date d = dueDate.getDate();
        cal.setTime(d);

        dueDateJson.put("minute", cal.get(Calendar.MINUTE));
        dueDateJson.put("hour", cal.get(Calendar.HOUR_OF_DAY));
        dueDateJson.put("day", cal.get(Calendar.DAY_OF_MONTH));
        dueDateJson.put("month", cal.get(Calendar.MONTH));
        dueDateJson.put("year", cal.get(Calendar.YEAR));

        return dueDateJson;
    }

    // EFFECTS: returns JSON representation of task
    public static JSONObject taskToJson(Task task) {
        JSONObject taskJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        taskJson.put("description", task.getDescription());

        Set<Tag> listofTags;
        listofTags = task.getTags();

        for (Tag t : listofTags) {
            jsonArray.put(tagToJson(t));
        }
        taskJson.put("tags", jsonArray);

        if (dueDateToJson(task.getDueDate()) == null) {
            taskJson.put("due-date", JSONObject.NULL);
        } else {
            taskJson.put("due-date", dueDateToJson(task.getDueDate()));
        }

        taskJson.put("priority", priorityToJson(task.getPriority()));
        taskJson.put("status", Jsonifier.underscoreStatus(task.getStatus()));

        return taskJson;
    }

    // EFFECTS: returns JSON array representing list of tasks
    public static JSONArray taskListToJson(List<Task> tasks) {
        JSONArray jsonArray = new JSONArray();

        for (Task t: tasks) {
            jsonArray.put(taskToJson(t));
        }

        return jsonArray;
    }

    private static String underscoreStatus(Status s) {
        if (s.equals(Status.IN_PROGRESS) || s.equals(Status.UP_NEXT)) {
            return s.toString().replace(" ", "_");
        }

        return s.toString();
    }

    private static String noHashTagTag(Tag t) {
        return t.toString().replace("#", "");
    }

}
