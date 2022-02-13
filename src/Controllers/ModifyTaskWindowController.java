package Controllers;

import Models.Task;
import Models.Window;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;

public class ModifyTaskWindowController extends MainWindowController{
    @FXML private TextField modifiedTitle;
    @FXML private ChoiceBox <String> priorityChoiceBox;
    @FXML private DatePicker modifiedDeadline;
    @FXML private TextArea modifiedDescription;
    private Task task;
    public void transferData(int id, String title, String description, int priority, String date, int status){
        this.task = new Task(id, priority, status, description, title, date);
        this.loadDataToFields();
    }
    @FXML private void initialize(){
        this.loadItemsToChoiceBox();
    }
    /*Sets possible choices*/
    private void loadItemsToChoiceBox(){
        this.priorityChoiceBox.getItems().add("Low");
        this.priorityChoiceBox.getItems().add("Medium");
        this.priorityChoiceBox.getItems().add("High");
    }
    /*Load data to fields*/
    private void loadDataToFields(){
        this.modifiedTitle.setText(this.task.getTitle());
        if(this.task.getPriority()==1)this.priorityChoiceBox.setValue("Low");
        else if(this.task.getPriority()==2)this.priorityChoiceBox.setValue("Medium");
        else if(this.task.getPriority()==3)this.priorityChoiceBox.setValue("High");
        this.modifiedDeadline.setValue(LocalDate.parse(this.task.getDate()));
        this.modifiedDescription.setText(this.task.getDescription());
    }
    /*Checks if inserted title and description are not too long
and sets default tile and description if there is none*/
    private boolean checkIfAllInsertedDataIsCorrectAndFixIt(){
        if(this.modifiedTitle.getText().length()>50){
            this.createPopUpWindow("Title is too big");
            return false;
        }
        else if(this.modifiedDescription.getText().length()>255){
            this.createPopUpWindow("Description is too big");
            return false;
        }
        if(this.modifiedTitle.getText().length()==0)this.modifiedTitle.setText("There is a lack of title");
        if(this.modifiedDescription.getText().length()==0)this.modifiedDescription.setText("There is a lack of description");
        return true;
    }
    /*Inserts data to the database*/
    public void sendModifiedTaskToDatabase(){
        if(this.checkIfAllInsertedDataIsCorrectAndFixIt()){
            try{
                Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kanban", "root", "");
                Statement myStat = myConn.createStatement();
                int priorityNumber = switch (this.priorityChoiceBox.getValue()) {
                    case "Low" -> 1;
                    case "Medium" -> 2;
                    case "High" -> 3;
                    default -> 0;
                };
                myStat.execute("UPDATE tasks SET title = '"+this.modifiedTitle.getText()+"', description = '"+this.modifiedDescription.getText()+"', priority = "+priorityNumber+", date = '"+this.modifiedDeadline.getValue()+"' WHERE id="+this.task.getId()+";");
                this.createPopUpWindow("Task has been modified");
            }
            catch(Exception exc){
                exc.printStackTrace();
            }
        }
    }
    private void createPopUpWindow(String message){
        Window newWindow = new Window("PopUp Window", "/Views/PopUpWindow.fxml", "/styles/mainStyle.css", "/data/photos/popUpIcon.png",  235, 92);
        newWindow.initWindow();
        PopUpWindowController scene4Controller = newWindow.getLoader().getController();
        scene4Controller.transferMessage(message);
        newWindow.showWindow();
    }
}
