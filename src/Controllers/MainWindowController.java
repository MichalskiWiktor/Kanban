package Controllers;

import Models.Database;
import Models.Window;
import Models.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

///// In projects section a list of projects title description img technology etc
public class MainWindowController{
    @FXML private ListView <Task> toDoList;
    @FXML private ListView <Task> inProgressList;
    @FXML private ListView <Task> doneList;
    @FXML private ImageView closeImg;
    @FXML private ImageView minimizeImg;
    @FXML private MenuButton upcomingBtn;
    @FXML private MenuButton inworkBtn;
    @FXML private MenuButton completeBtn;
    @FXML private Pane topPane;
    private double xOffset = 0;
    private double yOffset = 0;
    private final Database database = new Database ();

    @FXML private void initialize(){
        this.loadDataToLists();
        this.loadImg();
        this.waitEvent();
    }
    public void loadImg(){
        Image image = new Image("/data/photos/dotsIcon.png");
        ImageView imageView = new ImageView(image);
        ImageView imageView2 = new ImageView(image);
        ImageView imageView3 = new ImageView(image);
        upcomingBtn.setGraphic(imageView);
        inworkBtn.setGraphic(imageView2);
        completeBtn.setGraphic(imageView3);
    }
    public void waitEvent(){
        this.closeImg.setOnMousePressed (event ->{
            Stage stage = (Stage) this.toDoList.getScene().getWindow();
            stage.close();
        });
        this.minimizeImg.setOnMousePressed (event ->{
            Stage stage = (Stage) this.toDoList.getScene().getWindow();
            stage.setIconified(true);
        });

        this.topPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        this.topPane.setOnMouseDragged(event -> {
            Stage stage = (Stage)topPane.getScene ().getWindow ();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
    /*Inserts data from order list into listviews*/
    private void loadDataToLists(){
        this.toDoList.setCellFactory((lv) -> TaskListCell.newInstance());
        this.inProgressList.setCellFactory((lv) -> TaskListCell.newInstance());
        this.doneList.setCellFactory((lv) -> TaskListCell.newInstance());
        ObservableList<Task> tasksToDo = FXCollections.observableArrayList();
        ObservableList<Task> tasksInProgresses = FXCollections.observableArrayList();
        ObservableList<Task> tasksDone = FXCollections.observableArrayList();
        for(Task task : this.database.getTasks ()) {
            if (task.getStatus() == 1) {
                tasksToDo.add(task);
                this.toDoList.setItems(tasksToDo);
            } else if (task.getStatus() == 2) {
                tasksInProgresses.add(task);
                this.inProgressList.setItems(tasksInProgresses);
            } else if (task.getStatus() == 3) {
                tasksDone.add(task);
                this.doneList.setItems(tasksDone);
            }
        }
    }
    /*Makes sure only one item is selected*/
    @FXML public void handleMouseClickToDoList() {
        this.inProgressList.getSelectionModel().clearSelection();
        this.doneList.getSelectionModel().clearSelection();
    }
    @FXML public void handleMouseClickInProgressList() {
        this.toDoList.getSelectionModel().clearSelection();
        this.doneList.getSelectionModel().clearSelection();
    }
    @FXML public void handleMouseClickDoneList() {
        this.toDoList.getSelectionModel().clearSelection();
        this.inProgressList.getSelectionModel().clearSelection();
    }
    public void refreshLists(){
        this.clearAllListviews();
        this.loadDataToLists();
    }
    private void clearAllListviews(){
        this.toDoList.getItems().clear();
        this.inProgressList.getItems().clear();
        this.doneList.getItems().clear();
    }
    public void openSettings(){
        Window newWindow = new Window("Settings", "/Views/settingsWindow.fxml", "/styles/style.css", "/data/photos/miniSettings.png",  370, 350);
        newWindow.initWindow();
        newWindow.showWindow();
    }
    /*After button is clicked it creates new window where we can add new task*/
    public void addNewTask(){
        /*Window newWindow = new Window("Add New Task", "/Views/AddTaskWindow.fxml", "/styles/mainStyle.css", null,  342, 353);
        newWindow.initWindow();
        newWindow.showAndWaitWindow();
        this.refreshLists();*/
    }
    /*After button is clicked it creates new window where it shows details about selected task*/
   /* public void showTask(){
        Task task = this.findSelectedTask();
        if(task !=null){
            Window newWindow = new Window("Details", "/Views/showDetailsWindow.fxml", "/styles/mainStyle.css", null, 358, 255);
            newWindow.initWindow();
            ShowDetailsWindowController scene4Controller = newWindow.getLoader().getController();
            scene4Controller.transferData(task.getId(), task.getTitle(), task.getDescription(), task.getPriority(), task.getDate(), task.getStatus());
            newWindow.showAndWaitWindow();
        }
        else this.createPopUpWindow("You have to pick an element!!");
    }*/
    /*After button is clicked it creates new window where we can modify selected item*/
    /*public void modifyTask(){
        Task task = this.findSelectedTask();
        if(task !=null){
            Window newWindow = new Window("Modify Task", "/Views/ModifyTaskWindow.fxml", "/styles/mainStyle.css", null, 342, 353);
            newWindow.initWindow();
            ModifyTaskWindowController scene4Controller = newWindow.getLoader().getController();
            scene4Controller.transferData(task.getId(), task.getTitle(), task.getDescription(), task.getPriority(), task.getDate(), task.getStatus());
            newWindow.showAndWaitWindow();
            this.refreshLists();
        }
        else this.createPopUpWindow("You have to pick an element!!");
    }*/
    /*After button is clicked selected item is deleted*/
    /*public void deleteTask(){
        Task task = this.findSelectedTask();
        if(task !=null){
            try{
                this.database.runQuery(("DELETE FROM tasks WHERE id=" + task.getId()));
                this.refreshLists();
            }catch(Exception exc){
                exc.printStackTrace();
            }
        }
        else this.createPopUpWindow("You have to pick an element!!");
    }*/
    /*After button is clicked selected item is moved to the next listview and if it is in "done list" it will do nothing*/
    public void moveTask(){
        Task task = this.findSelectedTask();
        if(task !=null){
            try{
                this.database.runQuery(("UPDATE tasks SET status = " + (task.getStatus() + 1) + " WHERE id=" + task.getId() + " AND status<>3"));
                this.refreshLists();
            }catch(Exception exc){
                exc.printStackTrace();
            }
        }
        //else this.createPopUpWindow("You have to pick an element!!");
    }
    /*Returns selected item*/
    private Task findSelectedTask(){
        Task task;
        if(this.database.getTasks ().contains(this.toDoList.getSelectionModel().getSelectedItem()))
            task = this.toDoList.getSelectionModel().getSelectedItem();
        else if(this.database.getTasks ().contains(this.inProgressList.getSelectionModel().getSelectedItem()))
            task = this.inProgressList.getSelectionModel().getSelectedItem();
        else if(this.database.getTasks ().contains(this.doneList.getSelectionModel().getSelectedItem()))
            task = this.doneList.getSelectionModel().getSelectedItem();
        else task = null;
        return task;
    }
    /*private void createPopUpWindow(String message){
        Window newWindow = new Window("PopUp Window", "/Views/PopUpWindow.fxml", "/styles/mainStyle.css", "/data/photos/popUpIcon.png", 235, 92);
        newWindow.showAndWaitWindow();
        PopUpWindowController scene4Controller = newWindow.getLoader().getController();
        scene4Controller.transferMessage(message, null);
    }*/
}