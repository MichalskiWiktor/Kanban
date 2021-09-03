package Controllers;

import Models.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import Models.Task;
import javafx.stage.Stage;

public class MainWindowController{
    @FXML private ListView <Task> toDoList;
    @FXML private ListView <Task> inProgressList;
    @FXML private ListView <Task> doneList;
    @FXML private Button refreshBtn;
    private final ArrayList <Task> tasks = new ArrayList<>();

    @FXML private void initialize(){
        this.getDataFromDatabase();
        this.loadDataToLists();
        this.initPhotos();
    }
    /*Sets default photos*/
    private void initPhotos(){
        ImageView imageView = new ImageView(getClass().getResource("/data/photos/refresh.png").toExternalForm());
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        imageView.setY(10);
        this.refreshBtn.setGraphic(imageView);
    }
    /*Makes connection to database*/
    private ResultSet connectToDatabase(String query){
        try{
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kanban", "root", "");
            Statement myStat = myConn.createStatement();
            if(query.startsWith("UPDATE") || query.startsWith("DELETE"))myStat.executeUpdate(query);
            else return myStat.executeQuery(query);
        }
        catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    public void databaseConnectionError(){
        this.createPopUpWindow("Database error!!!");
    }
    /*Gets data from database and inserts it into order list*/
    private void getDataFromDatabase(){
        try{
            ResultSet myRes = this.connectToDatabase("SELECT * FROM orders");
            while(myRes.next())
                this.tasks.add(new Task(myRes.getInt("id"), myRes.getInt("priority"), myRes.getInt("status"), myRes.getString("description"), myRes.getString("title"), myRes.getString("date")));
        }
        catch(Exception exc){
            this.databaseConnectionError();
            Stage stage = (Stage) this.toDoList.getScene().getWindow();
            stage.close();
            exc.printStackTrace();
        }
    }
    /*Inserts data from order list into listviews*/
    private void loadDataToLists(){
        this.toDoList.setCellFactory((lv) -> TaskListCell.newInstance());
        this.inProgressList.setCellFactory((lv) -> TaskListCell.newInstance());
        this.doneList.setCellFactory((lv) -> TaskListCell.newInstance());
        ObservableList<Task> tasksToDo = FXCollections.observableArrayList();
        ObservableList<Task> tasksInProgresses = FXCollections.observableArrayList();
        ObservableList<Task> tasksDone = FXCollections.observableArrayList();
        for(Task task : this.tasks) {
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
        this.tasks.clear();
        this.getDataFromDatabase();
        this.loadDataToLists();
    }
    private void clearAllListviews(){
        this.toDoList.getItems().clear();
        this.inProgressList.getItems().clear();
        this.doneList.getItems().clear();
    }
    /*After button is clicked it creates new window where we can add new task*/
    public void addNewTask(){
        Window newWindow = new Window("Add New Task", "/Views/AddNewTaskWindow.fxml", "/styles/style2.css", 342, 353);
        newWindow.showWindow();
    }
    /*After button is clicked it creates new window where it shows details about selected task*/
    public void showDetailsWindow(){
        Task task = this.findSelectedTask();
        if(task !=null){
            Window newWindow = new Window("Details", "/Views/showDetailsWindow.fxml", "/styles/style.css", 358, 255);
            newWindow.showWindow();
            ShowDetailsWindowController scene4Controller = newWindow.getLoader().getController();
            scene4Controller.transferData(task.getId(), task.getTitle(), task.getDescription(), task.getPriority(), task.getDate(), task.getStatus());
        }
        else this.createPopUpWindow("You have to pick an element!!");
    }
    /*After button is clicked it creates new window where we can modify selected item*/
    public void modifyTask(){
        Task task = this.findSelectedTask();
        if(task !=null){
            Window newWindow = new Window("Modify Task", "/Views/ModifyTaskWindow.fxml", "/styles/style2.css", 342, 353);
            newWindow.showWindow();
            ModifyTaskWindowController scene4Controller = newWindow.getLoader().getController();
            scene4Controller.transferData(task.getId(), task.getTitle(), task.getDescription(), task.getPriority(), task.getDate(), task.getStatus());
        }
        else this.createPopUpWindow("You have to pick an element!!");
    }
    /*After button is clicked selected item is deleted*/
    public void deleteTask(){
        Task task = this.findSelectedTask();
        if(task !=null){
            try{
                this.connectToDatabase(("DELETE FROM tasks WHERE id=" + task.getId()));
                this.refreshLists();
            }catch(Exception exc){
                exc.printStackTrace();
            }
        }
        else this.createPopUpWindow("You have to pick an element!!");
    }
    /*After button is clicked selected item is moved to the next listview and if it is in "done list" it will do nothing*/
    public void moveTask(){
        Task task = this.findSelectedTask();
       if(task !=null){
           try{
               this.connectToDatabase(("UPDATE tasks SET status = " + (task.getStatus() + 1) + " WHERE id=" + task.getId() + " AND status<>3"));
               this.refreshLists();
           }catch(Exception exc){
               exc.printStackTrace();
           }
       }
       else this.createPopUpWindow("You have to pick an element!!");
    }
    /*Returns selected item*/
    private Task findSelectedTask(){
        Task task;
        if(this.tasks.contains(this.toDoList.getSelectionModel().getSelectedItem()))
            task = this.toDoList.getSelectionModel().getSelectedItem();
        else if(this.tasks.contains(this.inProgressList.getSelectionModel().getSelectedItem()))
            task = this.inProgressList.getSelectionModel().getSelectedItem();
        else if(this.tasks.contains(this.doneList.getSelectionModel().getSelectedItem()))
            task = this.doneList.getSelectionModel().getSelectedItem();
        else task = null;
        return task;
    }
    private void createPopUpWindow(String message){
        Window newWindow = new Window("PopUp Window", "/Views/PopUpWindow.fxml", "/styles/style.css", 235, 92);
        newWindow.showWindow();
        PopUpWindowController scene4Controller = newWindow.getLoader().getController();
        scene4Controller.transferMessage(message, null);
    }
}