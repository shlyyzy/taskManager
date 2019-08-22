package parsers;

import model.DueDate;
import model.Status;
import model.Task;
import parsers.exceptions.ParsingException;

import java.util.*;

public class TagParser extends Parser {

    private String input;
    private Task task;
    private List<String> tagList;
    private int descriptionIndex;
    private DueDate today;
    private DueDate tomorrow;
    private List<String> statusTags;
    private List<String> duplicateTags;

    public TagParser() {
        today = new DueDate();
        tomorrow = new DueDate();
        tomorrow.postponeOneDay();
    }

    // MODIFIES: this, task
    // EFFECTS: parses the input to extract properties such as priority or deadline
    //    and updates task with those extracted properties
    //  throws ParsingException if description does not contain the tag deliminator
    @Override
    public void parse(String input, Task task) throws ParsingException {
        this.input = input;
        this.task = task;

        tagList = new ArrayList<>();

        if (!(input.contains("##"))) {
            description = input;
            task.setDescription(input);
            throw new ParsingException("Doesn't contain tag delimiter");
        }

        descriptionIndex = input.indexOf("##");
        description = input.substring(0, descriptionIndex);
        task.setDescription(description);

        if (input.length() > descriptionIndex + 3) {
            setParseDescription();
        }
    }

    // MODIFIES: this, task
    // EFFECTS: updates ALL states in Task if there are actual tags in description that update them
    private void setParseDescription() {
        String restOfSet = input.substring(descriptionIndex + 2);
        if (restOfSet.contains(";")) {
            String[] splitString = restOfSet.split(";");

            for (String a : splitString) {
                if (!(a.trim().isEmpty())) {
                    tagList.add(a.trim());
                }
            }
            doAll();
        } else if (!(restOfSet.isEmpty())) {
            tagList.add(restOfSet.trim());
            doAll();
        }
    }

    // MODIFIES: this, task
    // EFFECTS: updates all of Task's features if specified in description
    public void doAll() {
        containsDate();
        containsStatus();
        containsPriority();
        dowithTags();
    }

    // MODIFIES: this, task
    // EFFECTS: updates Task due date if specified in description
    private void containsDate() {
        boolean found = false;
        List<String> toRemove = new ArrayList<>();

        while (!found) {
            for (String a : tagList) {
                if (a.equalsIgnoreCase("today")) {
                    task.setDueDate(today);
                    toRemove.add(a);
                    break;
                } else if (a.equalsIgnoreCase("tomorrow")) {
                    task.setDueDate(tomorrow);
                    toRemove.add(a);
                    break;
                }
            }
            found = true;
        }
        tagList.removeAll(toRemove);
    }

    // MODIFIES: this, task
    // EFFECTS: adds Task status tags to list to be updated if specified in description
    private void containsStatus() {
        statusTags = new ArrayList<>();
        if (tagList.size() != 0) {
            for (String a : tagList) {
                if (a.equalsIgnoreCase("to do")) {
                    statusTags.add(a);
                } else if (a.equalsIgnoreCase("up next")) {
                    statusTags.add(a);
                } else if (a.equalsIgnoreCase("in progress")) {
                    statusTags.add(a);
                } else if (a.equalsIgnoreCase("done")) {
                    statusTags.add(a);
                }
            }
            if (statusTags.size() != 0) {
                setStatusParser();
            }
        }
    }

    // MODIFIES: this, task
    // EFFECTS: updates Task's status if specified in description
    private void setStatusParser() {
        String a = statusTags.get(0);
        if (a.equalsIgnoreCase("to do")) {
            task.setStatus(Status.TODO);
            tagList.remove(a);
        } else if (a.equalsIgnoreCase("up next")) {
            task.setStatus(Status.UP_NEXT);
            tagList.remove(a);
        } else if (a.equalsIgnoreCase("in progress")) {
            task.setStatus(Status.IN_PROGRESS);
            tagList.remove(a);
        } else {
            task.setStatus(Status.DONE);
            tagList.remove(a);
        }
        removeDuplicatesStatus(a);
    }

    // MODIFIES: this
    // EFFECTS: remove duplicate statuses of the status being set
    private void removeDuplicatesStatus(String a) {
        duplicateTags = new ArrayList<>();
        for (String x : tagList) {
            if (a.equalsIgnoreCase(x)) {
                duplicateTags.add(x);
            }
        }
        tagList.removeAll(duplicateTags);
    }

    // MODIFIES: this, task
    // EFFECTS: updates Task priority if specified in description, no duplicates
    private void containsPriority() {
        List<String> toRemove = new ArrayList<>();
        for (String a : tagList) {
            if (a.equalsIgnoreCase("important")) {
                task.getPriority().setImportant(true);
                toRemove.add(a);
            } else if (a.equalsIgnoreCase("urgent")) {
                task.getPriority().setUrgent(true);
                toRemove.add(a);
            }
        }
        tagList.removeAll(toRemove);
    }

    // MODIFIES: this, task
    // EFFECTS: adds any tags (case insensitive), no duplicates, to task's list of tags
    private void dowithTags() {
        for (String a : tagList) {
            task.addTag(a);
        }
    }
}

