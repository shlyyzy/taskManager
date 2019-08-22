package utility;

import model.Task;
import org.json.JSONArray;
import parsers.TaskParser;
import persistence.Jsonifier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// File input/output operations
public class JsonFileIO {
    public static final File jsonDataFile = new File("./resources/json/tasks.json");
    
    // EFFECTS: attempts to read jsonDataFile and parse it
    //          returns a list of tasks from the content of jsonDataFile
    public static List<Task> read() {

        TaskParser taskParser = new TaskParser();
        List<Task> taskArray;

        try {
            Scanner fileString = new Scanner(jsonDataFile);
            String taskString = fileString.useDelimiter("\\Z").next(); // converted jsonDataFile to string using Scanner
            // https://stackoverflow.com/questions/3402735/what-is-simplest-way-to-read-a-file-into-string
            taskArray = taskParser.parse(taskString);
        } catch (IOException e) {
            taskArray = new ArrayList<>();
            System.out.println("Exception thrown!");
        }

        return taskArray;
    }
    
    // EFFECTS: saves the tasks to jsonDataFile
    public static void write(List<Task> tasks) {
        Jsonifier jsonifier = new Jsonifier();
        TaskParser jsonArrayTask = new TaskParser();

        JSONArray taskArray = jsonifier.taskListToJson(tasks);
        String taskString = taskArray.toString(4);

        try {
            BufferedWriter openFile = new BufferedWriter(new FileWriter(jsonDataFile));
            openFile.write(taskString);
            openFile.close();
        } catch (IOException e) {
            System.out.println("IOException thrown!");
        }

    }
}
