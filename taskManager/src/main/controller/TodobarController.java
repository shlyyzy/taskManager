package controller;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import model.Task;
import ui.AddTask;
import ui.EditTask;
import ui.ListView;
import ui.PomoTodoApp;
import utility.JsonFileIO;
import utility.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

// Controller class for Todobar UI
public class TodobarController implements Initializable {
    private static final String todoOptionsPopUpFXML = "resources/fxml/TodoOptionsPopUp.fxml";
    private static final String todoActionsPopUpFXML = "resources/fxml/TodoActionsPopUp.fxml";
    private File todobarOptionsPopUpFxmlFile = new File(todoOptionsPopUpFXML);
    private File todobarActionsPopUpFxmlFile = new File(todoActionsPopUpFXML);
    
    @FXML
    private Label descriptionLabel;
    @FXML
    private JFXHamburger todoActionsPopUpBurger;
    @FXML
    private StackPane todoActionsPopUpContainer;
    @FXML
    private JFXRippler todoOptionsPopUpRippler;
    @FXML
    private StackPane todoOptionsPopUpBurger;
    
    private Task task;
    private JsonFileIO jsonFileIO;
    private List<Task> taskList;

    private JFXPopup todoOptionPopUp;
    private JFXPopup todoActionPopUp;

    // REQUIRES: task != null
    // MODIFIES: this
    // EFFECTS: sets the task in this Todobar
    //          updates the Todobar UI label to task's description
    public void setTask(Task task) {
        this.task = task;
        descriptionLabel.setText(task.getDescription());
        jsonFileIO = new JsonFileIO();
        //taskList = jsonFileIO.read();
        taskList = PomoTodoApp.getTasks();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadToDoBarActionsPopUp();
        loadActionsPopUpActionListener();
        loadToDoBarOptionsPopUp();
        loadOptionsPopUpActionListener();
    }

    // EFFECTS: load options popup (to-do, in progress, up next, done, pomodoro)
    private void loadToDoBarOptionsPopUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(todobarOptionsPopUpFxmlFile.toURI().toURL());
            fxmlLoader.setController(new OptionPopUpController());
            todoOptionPopUp = new JFXPopup(fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    // EFFECTS: load options popup (to-do, in progress, up next, done, pomodoro)
    private void loadToDoBarActionsPopUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(todobarActionsPopUpFxmlFile.toURI().toURL());
            fxmlLoader.setController(new ActionPopUpController());
            todoActionPopUp = new JFXPopup(fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    // EFFECTS: show actions selector pop up when its icon is clicked
    private void loadActionsPopUpActionListener() {
        todoActionsPopUpBurger.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                todoActionPopUp.show(todoActionsPopUpBurger,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.LEFT,
                        12,
                        15);
            }
        });
    }

    // EFFECTS: show options selector pop up when its icon is clicked
    private void loadOptionsPopUpActionListener() {
        todoOptionsPopUpBurger.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                todoOptionPopUp.show(todoOptionsPopUpBurger,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.LEFT,
                        12,
                        15);
            }
        });
    }

    // Inner class: options selector pop up controller
    class OptionPopUpController {
        @FXML
        private JFXListView<?> optionPopUpList;

        @FXML
        private void submit() {
            int selectedIndex = optionPopUpList.getSelectionModel().getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    PomoTodoApp.setScene(new EditTask(task));
                    jsonFileIO.write(taskList);
                    Logger.log("TodobarActionsPopUpController", "Edited!");
                    break;
                case 1:
                    taskList.remove(task);
                    jsonFileIO.write(taskList);
                    PomoTodoApp.setScene(new ListView(taskList));
                    Logger.log("TodobarActionsPopUpController", "Deleted!");
                    break;
                default:
                    Logger.log("TodobarActionsPopUpController", "No action is implemented for the selected option");
            }
            todoOptionPopUp.hide();
        }
    }

    // Inner class: actions selector pop up controller
    class ActionPopUpController {
        @FXML
        private JFXListView<?> actionPopUpList;

        @FXML
        private void submit() {
            int selectedIndex = actionPopUpList.getSelectionModel().getSelectedIndex();
            switch (selectedIndex) {
                case 0: Logger.log("TodobarActionsPopUpController", "To Do not supported");
                    break;
                case 1: Logger.log("TodobarActionsPopUpController", "Up Next not supported");
                    break;
                case 2: Logger.log("TodobarActionsPopUpController", "In Progress not supported");
                    break;
                case 3: Logger.log("TodobarActionsPopUpController", "Done not supported");
                    break;
                case 4: Logger.log("TodobarActionsPopUpController", "Pomodoro not supported");
                    break;
                default: Logger.log("TodobarActionsPopUpController", "No action is implemented for option");
            }
            todoActionPopUp.hide();
        }
    }

}
